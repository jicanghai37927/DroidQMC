package io.bunnyblue.droidncm.finder.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;

import io.bunnyblue.droidncm.finder.MainFinderActivity;
import io.bunnyblue.droidncm.finder.dummy.NCMFileContent;

public class NCMFileFinder extends AsyncTask<File, String, NCMFileContent> {
    ProgressDialog progressDialog;
    Context context;

    public NCMFileFinder(Context context) {
        this.context = context;
    }

    @SuppressLint("WrongThread")
    @Override
    protected NCMFileContent doInBackground(File... files) {
        if (files != null) {
            NCMFileContent ncmFileContent = new NCMFileContent();
            for (File file : files) {
                if (!file.exists()){
                    continue ;
                }

                Collection<File> fileCollection = FileUtils.listFiles(file,
                        new String[]{
                                "qmcflac", "qmc3", "qmc0",
                                "QMCFLAC", "QMC3", "QMC0"
                        },
                        true);

                for (File localFile :
                        fileCollection) {
                    NCMFileContent.NCMLocalFile ncmLocalFile = new NCMFileContent.NCMLocalFile();
                    ncmLocalFile.localPath = localFile.getAbsolutePath();
                    ncmLocalFile.content = localFile.getName();
                    ncmFileContent.addFile(ncmLocalFile);
                    onProgressUpdate(ncmLocalFile.content);

                }
            }

            return ncmFileContent;


        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //  ProgressDialog  builder = new ProgressDialog.Builder(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("searching...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
            }
        });

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
            }
        });
    }

    @Override
    protected void onProgressUpdate(final String... values) {
        super.onProgressUpdate(values);

        ((MainFinderActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage(values[0]);
            }
        });
    }

    @Override
    protected void onPostExecute(NCMFileContent ncmFileContent) {
        super.onPostExecute(ncmFileContent);
        //   progressDialog.dismiss();
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
            }
        });
        if (ncmFileContent == null) {
            Toast.makeText(context, "没有找到文件", Toast.LENGTH_SHORT).show();
        } else {
            ((MainFinderActivity) context).ncmFileContent = ncmFileContent;
            ((MainFinderActivity) context).updateNCMFileList();
        }

    }
}
