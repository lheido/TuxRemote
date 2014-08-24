package com.tuxremote.app;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tuxremote.app.TuxeRemoteSsh.BashReturn;

import java.util.ArrayList;

public abstract class FileSelectorDialog extends Dialog {

    private final ProgressBar progress;
    private ArrayList<File> fileList;
    private FileListAdapter adapter;
    private ListView fileListView;
    private String currentDir;
    private String currentParent;

    public FileSelectorDialog(Context context) {
        super(context);
        setContentView(R.layout.file_selector);
        setTitle("Selection fichier");
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setIndeterminate(true);
        progress.setVisibility(View.GONE);
        fileListView = (ListView) findViewById(R.id.file_list);
        fileList = new ArrayList<File>();
        adapter = new FileListAdapter(context, fileList);
        fileListView.setAdapter(adapter);
        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                File file = fileList.get(i);
                if(file.isDir()){
                    String path = currentDir + file.getFileName();
                    loadFileList(path);
                }else if (file.getFileName().equals("..")){
                    loadFileList(currentParent);
                }else{
                    customItemClick(file, currentDir, currentParent);
                }
            }
        });
        currentDir = "";
        loadFileList(currentDir);
    }

    public abstract void customItemClick(File file, String currentDir, String currentParent);

    private void loadFileList(String dir){
        fileList.clear();
        SSHAsyncTask fileTask = new SSHAsyncTask(new Command("ls", "cd "+dir+"; ls -1p . | awk -v p=$(pwd) -v q=$(cd .. ; pwd) 'BEGIN{print p \"\\n\" q}{print $0}'", null)){
            @Override
            protected void onPreExecute () {
                super.onPreExecute();
                progress.setVisibility(View.VISIBLE);
            }
            @Override
            protected void onProgressUpdate (BashReturn... prog) {
                if(prog[0] != null){
                    ArrayList<String> liste = prog[0].getBashReturn();
                    currentDir = liste.remove(0)+"/";
                    currentParent = liste.remove(0)+"/";
                    liste.add(0, "..");
                    for (String line : liste){
                        fileList.add(getFileByLine(line));
                        adapter.notifyDataSetChanged();
                    }
                    fileListView.smoothScrollToPosition(0);
                }
                progress.setVisibility(View.GONE);
            }
        };
        fileTask.execTask();
    }

    private File getFileByLine(String line){
        File file = new File(line, line, false);
        if(line.contains("/"))
            file.setDir(true);
        return file;
    }

    public static class File {
        private String filePath = null;
        private String fileName = null;
        private boolean isDir = false;
        public File(String name, String filePath,boolean is_dir){
            this.fileName = name;
            this.filePath = filePath;
            this.isDir = is_dir;
        }

        public boolean isDir() {
            return isDir;
        }

        public void setDir(boolean isDir) {
            this.isDir = isDir;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFilePath() {
            return filePath;
        }
    }

    public static class FileListAdapter extends BaseAdapter {
        private final Context context;
        private ArrayList<File> files;

        public FileListAdapter(Context c, ArrayList<File> items){
            this.files = items;
            this.context = c;
        }
        @Override
        public int getCount() {
            return files.size();
        }

        @Override
        public Object getItem(int i) {
            return files.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // reuse views
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.row_file, parent, false);
                holder.text = (TextView) convertView.findViewById(R.id.label);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            // fill data
            File file = this.files.get(position);
            holder.text.setText(file.getFileName());
            if(file.isDir()){
                holder.text.setTextColor(context.getResources().getColor(R.color.dir_color));
            }else{
                holder.text.setTextColor(context.getResources().getColor(R.color.file_color));
            }

            return convertView;
        }

        private class ViewHolder {
            public TextView text;
        }
    }
}
