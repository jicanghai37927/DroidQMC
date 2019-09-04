package io.bunnyblue.droidncm.finder.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

import io.bunnyblue.droidncm.finder.MainFinderActivity;
import io.bunnyblue.droidncm.finder.dummy.NCMFileContent;
import io.bunnyblue.droidncm.utils.NcmdumpUtils;

public class OneConvertTask extends AsyncTask<NCMFileContent.NCMLocalFile, String, NCMFileContent.NCMLocalFile> {
    ProgressDialog progressDialog;
    Context context;

    public OneConvertTask(Context context) {
        this.context = context;
    }

    @Override
    protected NCMFileContent.NCMLocalFile doInBackground(NCMFileContent.NCMLocalFile... contents) {
        int index = 0;
        NCMFileContent.NCMLocalFile ncmLocalFile = contents[0];
        File srcFile = new File(ncmLocalFile.localPath);
        publishProgress(srcFile.getName());

        String targetFile = NcmdumpUtils.ncpDump(srcFile);
//        String targetFile = NcmDumper.ncpDump(srcFile.getAbsolutePath());
        if (targetFile.startsWith("/")) {
            File target = new File(targetFile);
            if (target.exists()) {
                ncmLocalFile.targetPath = targetFile;
                return ncmLocalFile;
            } else {
                ncmLocalFile.error = " 转换失败！";
            }
        } else {
            ncmLocalFile.error = targetFile;

            if (TextUtils.isEmpty(targetFile)) {
                ncmLocalFile.error = srcFile.getAbsolutePath() + " 转换失败！";
            }
        }

        return ncmLocalFile;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (values[0].startsWith("/")) {
            progressDialog.setMessage(String.format("success process  file %s", values[0]));
        } else {
            progressDialog.setMessage(String.format("processing file %s ..", values[0]));
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在处理");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(NCMFileContent.NCMLocalFile file) {
        super.onPostExecute(file);
        progressDialog.dismiss();
        if (!TextUtils.isEmpty(file.error)) {
            Toast.makeText(context, file.error, Toast.LENGTH_SHORT).show();
        }

        ((MainFinderActivity) context).updateNCMFileList();

    }

}
