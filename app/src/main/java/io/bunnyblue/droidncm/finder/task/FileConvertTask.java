package io.bunnyblue.droidncm.finder.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;

import io.bunnyblue.droidncm.finder.MainFinderActivity;
import io.bunnyblue.droidncm.finder.dummy.NCMFileContent;
import io.bunnyblue.droidncm.utils.NcmdumpUtils;

public class FileConvertTask extends AsyncTask<NCMFileContent, String, Integer> {
    ProgressDialog progressDialog;
    Context context;
    public FileConvertTask(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(NCMFileContent ...contents) {
        int index = 0;
        NCMFileContent content=contents[0];
        for (NCMFileContent.NCMLocalFile srcFile : content.getITEMS()) {
            File file=new File(srcFile.localPath);
            publishProgress(file.getName());
            String targetFile = NcmdumpUtils.ncpDump(file);
//            String targetFile = NcmDumper.ncpDump(file.getAbsolutePath());
            if (targetFile.startsWith("/"))
            {
                File target = new File(targetFile);
                if (target.exists()) {
                    publishProgress(target.getAbsolutePath());
                    srcFile.targetPath=target.getAbsolutePath();
                    index++;
                    //  return target;
                }
            }else {
                srcFile.error= targetFile;
            }
        }


        return index;
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
    protected void onPostExecute(Integer file) {
        super.onPostExecute(file);
        progressDialog.dismiss();
        ((MainFinderActivity) context).updateNCMFileList();
        if (file != 0) {
            Toast.makeText(context, "new file  count :" + file, Toast.LENGTH_SHORT).show();
        }
    }

}
