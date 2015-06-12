package com.lovejoy777.rroandlayersmanager1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.lovejoy777.rroandlayersmanager1.filepicker.FilePickerActivity;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by lovejoy777 on 08/06/15.
 */
public class Backup extends Activity {

    private static final int CODE_SD = 0;
    private static final int CODE_DB = 1;
    final String startDirInstalled = "/vendor/overlay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // GET STRING SZP
        final Intent extras = getIntent();
        String SZP = null;
        if (extras != null) {
            SZP = extras.getStringExtra("key1");
        }



        if (SZP != null) {

            final AlertDialog.Builder alert = new AlertDialog.Builder(Backup.this);
            alert.setIcon(R.drawable.chart);

            alert.setTitle("Options");
            alert.setMessage("delete or backup selected files.");
            alert.setPositiveButton("backup", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(Backup.this);
                    final EditText input = new EditText(Backup.this);
                    alert.setIcon(R.drawable.ic_backup);
                    alert.setTitle("LAYERS BACKUP");
                    alert.setMessage("");
                    alert.setView(input);
                    input.setHint("enter backup name");
                    alert.setInverseBackgroundForced(true);

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            // get editText String
                            String backupname = input.getText().toString();

                            if (backupname.length() <= 1) {

                                Toast.makeText(Backup.this, "INPUT A NAME", Toast.LENGTH_LONG).show();

                                finish();

                            } else {

                                File directory = new File("/vendor/overlay");
                                File[] contents = directory.listFiles();

                                // Folder is empty
                                if (contents.length == 0) {

                                    Toast.makeText(Backup.this, "NOTHING TO BACKUP", Toast.LENGTH_LONG).show();

                                    finish();
                                } else {
                                    try {

                                        String sdOverlays = Environment.getExternalStorageDirectory() + "/Overlays";

                                        // CREATES /SDCARD/OVERLAYS/BACKUP/TEMP
                                        File dir1 = new File(sdOverlays + "/Backup/temp");
                                        if (!dir1.exists() && !dir1.isDirectory()) {
                                            CommandCapture command = new CommandCapture(0, "mkdir " + sdOverlays + "/Backup/temp");
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
                                        File dir2 = new File(sdOverlays + "/Backup/" + backupname);
                                        if (!dir2.exists() && !dir2.isDirectory()) {
                                            CommandCapture command1 = new CommandCapture(0, "mkdir " + sdOverlays + "/Backup/" + backupname);
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

                                        // CHANGE PERMISSIONS OF /VENDOR/OVERLAY/ 666  && /VENDOR/OVERLAY 777 && /SDCARD/OVERLAYS/BACKUP/ 666
                                        CommandCapture command18 = new CommandCapture(0, "chmod 755 " + Environment.getExternalStorageDirectory() + "/Overlays/Backup/temp");
                                        RootTools.getShell(true).add(command18);
                                        while (!command18.isFinished()) {
                                            Thread.sleep(1);
                                        }
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

                                        Toast.makeText(Backup.this, "BACKUP COMPLETED", Toast.LENGTH_LONG).show();

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
        } else {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            // Set these depending on your use case. These are the defaults.
            i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
            i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
            i.putExtra(FilePickerActivity.EXTRA_START_PATH, startDirInstalled);

            // start filePicker forResult
            startActivityForResult(i, CODE_SD);
        }

    } // ends onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if ((CODE_SD == requestCode || CODE_DB == requestCode) &&
                resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE,
                    false)) {
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
                    Intent iIntent = new Intent(this, Backup.class);
                    iIntent.putExtra("key1", SZP);
                    iIntent.putStringArrayListExtra("key2", paths);
                    startActivity(iIntent);

                    finish();
                }

            } else {
                // Get the File path from the Uri
                String SZP = (data.getData().toString());
                if (SZP.startsWith("file://")) {
                    SZP = SZP.substring(7);
                    Intent iIntent = new Intent(this, Backup.class);
                    iIntent.putExtra("key1", SZP);
                    startActivity(iIntent);

                    finish();
                }
            }
        }
    } // ends onActivity for result

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

            Toast.makeText(Backup.this, "DELETED SELECTED LAYERS", Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(Backup.this, "NOTHING TO DELETE", Toast.LENGTH_LONG).show();
        }
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back2, R.anim.back1);
    }


}