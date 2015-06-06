package com.lovejoy777.rroandlayersmanager1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lovejoy777.rroandlayersmanager1.filepicker.FilePickerActivity;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class menu extends Activity {

    static final String TAG = "menu";


    final String startDirInstalled = "/vendor/overlay";
    final String startDirInstall = "/sdcard/Overlays";
    private static final int CODE_SD = 0;
    private static final int CODE_DB = 1;

    CardView card1, card2, card3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);



        // GET STRING FOR TEXT VIEW
        final Intent extras = getIntent();
        String SZP = null;
        if (extras != null) {
            SZP = extras.getStringExtra("key1");
        }

        if (SZP != null) {

            final AlertDialog.Builder alert = new AlertDialog.Builder(menu.this);
            alert.setIcon(R.drawable.chart);
            alert.setTitle("Options");
            alert.setMessage("backup or delete selected files.");
            alert.setPositiveButton("backup", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(menu.this);
                    final EditText input = new EditText(menu.this);
                    alert.setIcon(R.drawable.ic_backup);
                    alert.setTitle("LAYERS BACKUP");
                    alert.setMessage("");
                    alert.setView(input);
                    input.setHint("enter backup name");

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                           // get editText String
                           String backupname = input.getText().toString();

                            if (backupname.length() <= 1) {

                                Toast.makeText(menu.this, "INPUT A NAME", Toast.LENGTH_LONG).show();

                                finish();

                            } else {

                                File directory = new File("/vendor/overlay");
                                File[] contents = directory.listFiles();

                                // Folder is empty
                                if (contents.length == 0) {

                                    Toast.makeText(menu.this, "NOTHING TO BACKUP", Toast.LENGTH_LONG).show();

                                    finish();
                                }

                                // Folder contains files
                                else {
                                    try {

                                        String sdcard = "/sdcard/Overlays";

                                        // CREATES /SDCARD/OVERLAYS/BACKUP/TEMP
                                        File dir1 = new File(sdcard + "/Backup/temp");
                                        if (!dir1.exists() && !dir1.isDirectory()) {
                                            CommandCapture command = new CommandCapture(0, "mkdir " + sdcard + "/Backup/temp");
                                            try {
                                                RootTools.getShell(true).add(command);
                                                while (!command.isFinished()) {
                                                    Thread.sleep(1);
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (TimeoutException e) {
                                                e.printStackTrace();
                                            } catch (RootDeniedException e) {
                                                e.printStackTrace();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        // CREATES /SDCARD/OVERLAYS/BACKUP/BACKUPNAME
                                        File dir2 = new File(sdcard + "/Backup/" + backupname);
                                        if (!dir2.exists() && !dir2.isDirectory()) {
                                            CommandCapture command1 = new CommandCapture(0, "mkdir " + sdcard + "/Backup/" + backupname);
                                            try {
                                                RootTools.getShell(true).add(command1);
                                                while (!command1.isFinished()) {
                                                    Thread.sleep(1);
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (TimeoutException e) {
                                                e.printStackTrace();
                                            } catch (RootDeniedException e) {
                                                e.printStackTrace();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        RootTools.remount("/system", "RW");

                                        // CHANGE PERMISSIONS OF /VENDOR/OVERLAY && /SDCARD/OVERLAYS/BACKUP
                                        CommandCapture command2 = new CommandCapture(0,
                                                "chmod -R 755 /vendor/overlay",
                                                "chmod -R 755 " + Environment.getExternalStorageDirectory() + "/Overlays/Backup/",
                                                "cp -fr /vendor/overlay " + Environment.getExternalStorageDirectory() + "/Overlays/Backup/temp/");
                                        RootTools.getShell(true).add(command2);
                                        while (!command2.isFinished()) {
                                            Thread.sleep(1);
                                        }

                                        // ZIP OVERLAY FOLDER
                                        zipFolder(Environment.getExternalStorageDirectory() + "/Overlays/Backup/temp/overlay", Environment.getExternalStorageDirectory() + "/Overlays/Backup/" + backupname + "/overlay.zip");

                                        // DELETE /SDCARD/OVERLAYS/BACKUP/TEMP FOLDER
                                        RootTools.deleteFileOrDirectory(Environment.getExternalStorageDirectory() + "/Overlays/Backup/temp", true);

                                        // CHANGE PERMISSIONS OF /VENDOR/OVERLAY/ 666  && /VENDOR/OVERLAY 777 && /SDCARD/OVERLAYS/BACKUP/ 666
                                        CommandCapture command17 = new CommandCapture(0, "chmod -R 666 /vendor/overlay", "chmod 755 /vendor/overlay", "chmod -R 666" + Environment.getExternalStorageDirectory() + "/Overlays/Backup/");
                                        RootTools.getShell(true).add(command17);
                                        while (!command17.isFinished()) {
                                            Thread.sleep(1);
                                        }

                                        // CLOSE ALL SHELLS
                                        RootTools.closeAllShells();

                                        Toast.makeText(menu.this, "BACKUP COMPLETED", Toast.LENGTH_LONG).show();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (RootDeniedException e) {
                                        e.printStackTrace();
                                    } catch (TimeoutException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    finish();
                                }
                            }
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            dialog.cancel();

                        }
                    });

                    alert.show();

                }
            })
                    .setNegativeButton("delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // COMMAND 1 DELETE SELECTED LAYERS
                            deletemultiplecommand();

                        }
                    });

            alert.show();
        }

        String sdcard = "/sdcard/Overlays";

        // CREATES /SDCARD/OVERLAYS
        File dir = new File(sdcard);
        if (!dir.exists() && !dir.isDirectory()) {
            CommandCapture command3 = new CommandCapture(0, "mkdir " + sdcard);
            try {
                RootTools.getShell(true).add(command3);
                while (!command3.isFinished()) {
                    Thread.sleep(1);
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (RootDeniedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        String sdcard1 = "/sdcard/Overlays/Backup";
        // CREATES /SDCARD/OVERLAYS/BACKUP
        File dir1 = new File(sdcard1);
        if (!dir1.exists() && !dir1.isDirectory()) {
            CommandCapture command4 = new CommandCapture(0, "mkdir " + sdcard1);
            try {
                RootTools.getShell(true).add(command4);
                while (!command4.isFinished()) {
                    Thread.sleep(1);
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (RootDeniedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        RootTools.remount("/system", "RW");
        String vendover = "/vendor/overlay";
        // CREATES /VENDOR/OVERLAY
        File dir2 = new File(vendover);
        if (!dir2.exists() && !dir2.isDirectory()) {
            CommandCapture command5 = new CommandCapture(0, "mkdir " + vendover);
            try {
                RootTools.getShell(true).add(command5);
                while (!command5.isFinished()) {
                    Thread.sleep(1);
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (RootDeniedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        card1 = (CardView) findViewById(R.id.card_view1);
        card2 = (CardView) findViewById(R.id.card_view2);
        card3 = (CardView) findViewById(R.id.card_view3);

        // CARD 1
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                // Set these depending on your use case. These are the defaults.
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                i.putExtra(FilePickerActivity.EXTRA_START_PATH, startDirInstalled);

                // start filePicker forResult
                startActivityForResult(i, CODE_SD);

            } // end card1 onClick
        }); // end card 1

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                // Set these depending on your use case. These are the defaults.
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                i.putExtra(FilePickerActivity.EXTRA_START_PATH, startDirInstall);

                // start filePicker forResult
                startActivityForResult(i, CODE_SD);

            } // end card 2 onClick
        }); // end card 2

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(menu.this, "Button 3", Toast.LENGTH_LONG).show();
                rebootcommand(); // REBOOT DEVICE

            } // end card  onClick
        }); // end card 33
    } // end oncreate

    public void deletemultiplecommand () {

        ArrayList<String> paths;
        paths = getIntent().getStringArrayListExtra("key2");

        if (paths != null) {

            for (String path : paths) {
                if (path.startsWith("file://")) {
                    path = path.substring(7);

                    RootTools.remount("/system", "RW");
                    RootTools.deleteFileOrDirectory(path, true);
                }
            }

            Toast.makeText(menu.this, "DELETED SELECTED LAYERS", Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(menu.this, "NOTHING TO DELETE", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if ((CODE_SD == requestCode || CODE_DB == requestCode) &&
                resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE,
                    false)) {
                // 2
                ArrayList<String> paths = data.getStringArrayListExtra(
                        FilePickerActivity.EXTRA_PATHS);
                StringBuilder sb = new StringBuilder();

                if (paths != null) {
                    for (String path : paths) {
                        if (path.startsWith("file://")) {
                            path = path.substring(7);
                            sb.append(path);
                            // sb.append("\n");
                        }
                    }

                    String SZP = (sb.toString());
                    Intent iIntent = new Intent(this, menu.class);
                    iIntent.putExtra("key1", SZP);
                    iIntent.putStringArrayListExtra("key2", paths);
                    startActivity(iIntent);

                }

            } else {
                // 2
                // Get the File path from the Uri
                String SZP = (data.getData().toString());
                if (SZP.startsWith("file://")) {
                    SZP = SZP.substring(7);
                    Intent iIntent = new Intent(this, menu.class);
                    iIntent.putExtra("key1", SZP);
                    startActivity(iIntent);
                }
            }   // tvlayerspath.setText(data.getData().toString());
        }
    }

    // BACKUP COMMAND, BACKUP /VENDOR/OVERLAY FOLDER
    public void backupcommand() {




    }

    // REBOOT COMMAND, REBOOT DEVICE
    public void rebootcommand() {

        try {

            Toast.makeText(menu.this, "Rebooting", Toast.LENGTH_LONG).show();
            // MK DIR /SDCARD/OVERLAYS/BACKUP/TEMP
            CommandCapture command4 = new CommandCapture(0, "reboot");

            RootTools.getShell(true).add(command4);
            while (!command4.isFinished()) {
                Thread.sleep(1);

                finish();

            }
        } catch (RootDeniedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * **********************************************************************************************************
     * ZIP
     * ********
     * zip a zip file.
     *
     * @throws java.io.IOException *******************************************************************************
     */
    private static void zipFolder(String inputFolderPath, String outZipPath) {
        try {
            FileOutputStream fos = new FileOutputStream(outZipPath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File srcFile = new File(inputFolderPath);
            File[] files = srcFile.listFiles();
            Log.d("", "Zip directory: " + srcFile.getName());
            for (int i = 0; i < files.length; i++) {
                Log.d("", "Adding file: " + files[i].getName());
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(files[i]);
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        } catch (IOException ioe) {
            Log.e("", ioe.getMessage());
        }
    }

    /**
     * **********************************************************************************************************
     * UNZIP UTIL
     * ************
     * Unzip a zip file.
     *
     * @throws java.io.IOException ******************************************************************************
     */
    public void unzip(String zipFile, String location) throws IOException {

        int size;
        byte[] buffer = new byte[1024];

        try {

            if (!location.endsWith("/")) {
                location += "/";
            }
            File f = new File(location);
            if (!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), 1024));
            try {
                ZipEntry ze;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();
                    File unzipFile = new File(path);

                    if (ze.isDirectory()) {
                        if (!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {

                        // check for and create parent directories if they don't exist
                        File parentDir = unzipFile.getParentFile();
                        if (null != parentDir) {
                            if (!parentDir.isDirectory()) {
                                parentDir.mkdirs();
                            }
                        }
                        // unzip the file
                        FileOutputStream out = new FileOutputStream(unzipFile, false);
                        BufferedOutputStream fout = new BufferedOutputStream(out, 1024);
                        try {
                            while ((size = zin.read(buffer, 0, 1024)) != -1) {
                                fout.write(buffer, 0, size);
                            }
                            zin.closeEntry();
                        } finally {
                            fout.flush();
                            fout.close();
                            out.close();
                        }
                    }
                }
            } finally {
                zin.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Unzip exception", e);
        }
    }


}
