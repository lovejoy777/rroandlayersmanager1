package com.lovejoy777.rroandlayersmanager1;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.lovejoy777.rroandlayersmanager1.filepicker.FilePickerActivity;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class menu extends AppCompatActivity {

    static final String TAG = "menu";
    private static final int PROFILE_SETTING = 1;

    //save our header or result
    private AccountHeader.Result headerResult = null;
    private Drawer.Result result = null;


    final String startDirInstalled = "/vendor/overlay";
    final String startDirInstall = Environment.getExternalStorageDirectory() +  "/Overlays";
    private static final int CODE_SD = 0;
    private static final int CODE_DB = 1;

    CardView card1, card2, card3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create a few sample profile
        final IProfile profile = new ProfileDrawerItem().withName("BitSyko").withEmail("www.bitsyko.com").withIcon(getResources().getDrawable(R.mipmap.ic_launcher));

        // Create the AccountHeader
        headerResult = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header1)
                .withCompactStyle(true)
                .addProfiles(profile)
                .withSelectionListEnabledForSingleProfile(false)
                .withSelectionListEnabled(false)
                .withProfileImagesVisible(true)

                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {

                    @Override
                    public void onProfileChanged(View view, IProfile profile) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && ((IDrawerItem) profile).getIdentifier() == PROFILE_SETTING) {
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("lovejoy777").withEmail("salovejoy@gmail.com").withIcon(getResources().getDrawable(R.mipmap.ic_launcher));
                            //IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("lovejoy777").withEmail("salovejoy@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }
                    }
                })

                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer1
        result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withDisplayBelowToolbar(true)
                //.withSliderBackgroundColor(bgcolor)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(

                        new PrimaryDrawerItem().withName(R.string.drawer_item_about).withIcon(R.drawable.ic_about).withIdentifier(1).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_changelog).withIcon(R.drawable.ic_changelog).withIdentifier(2).withCheckable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_instructions).withIcon(R.drawable.info).withIdentifier(3).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(R.drawable.settings).withIdentifier(4).withCheckable(false),
                        new SectionDrawerItem().withName(R.string.drawer_item_links_header),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_gplus).withIcon(R.drawable.bitsyko_g_plus).withIdentifier(5).withCheckable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_xda).withIcon(R.drawable.xda).withIdentifier(6).withCheckable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_forum).withIcon(R.drawable.ic_forums).withIdentifier(7).withCheckable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_website).withIcon(R.drawable.ic_web).withIdentifier(8).withCheckable(false)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            if (drawerItem.getIdentifier() == 1) {
                                Intent intent = new Intent(menu.this, About.class);
                                menu.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 2) {
                                Intent intent = new Intent(menu.this, ChangeLog.class);
                                menu.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 3) {
                                Intent intent = new Intent(menu.this, Instructions.class);
                                menu.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 4) {
                                Intent intent = new Intent(menu.this, Settings.class);
                                menu.this.startActivity(intent);
                            } else if (drawerItem.getIdentifier() == 5) {
                                launchgplus();
                            } else if (drawerItem.getIdentifier() == 6) {
                                launchxda();
                            } else if (drawerItem.getIdentifier() == 7) {
                                launchforum();
                            } else if (drawerItem.getIdentifier() == 8) {
                                launchwebsite();

                            }
                        }
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        // set the selection to the item with the identifier 0
        result.setSelectionByIdentifier(0, false);
        headerResult.setActiveProfile(profile);





        // IF ROOT ACCESS IS GIVEN / ELSE LAUNCH PLAYSTORE FOR SUPERUSER APP
        if (!RootTools.isAccessGiven()) {
            Toast.makeText(menu.this, "Your device doesn't seem to be rooted", Toast.LENGTH_LONG).show();
            Intent intent0 = new Intent();
            intent0.setClass(this, PlaystoreSuperUser.class);
            startActivity(intent0);
            finish();

        } else {

            String sdOverlays = Environment.getExternalStorageDirectory() + "/Overlays";
            String sdcard = Environment.getExternalStorageDirectory() + "";
            String vendor = "/vend";

            RootTools.remount(sdcard, "RW");

            // CREATES /SDCARD/OVERLAYS
            File dir = new File(sdOverlays);
            if (!dir.exists() && !dir.isDirectory()) {
                CommandCapture command3 = new CommandCapture(0, "mkdir " + sdOverlays);
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

            String sdOverlays1 = Environment.getExternalStorageDirectory() + "/Overlays/Backup";
            // CREATES /SDCARD/OVERLAYS/BACKUP
            File dir1 = new File(sdOverlays1);
            if (!dir1.exists() && !dir1.isDirectory()) {
                CommandCapture command4 = new CommandCapture(0, "mkdir " + sdOverlays1);
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


                    Intent menuactivity = new Intent(menu.this, Install.class);

                    Bundle bndlanimation =
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anni1, R.anim.anni2).toBundle();
                    startActivity(menuactivity, bndlanimation);



                }
            }); // end card 1

            card2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  //  Intent intent = new Intent(menu.this, Backup.class);
                   // menu.this.startActivity(intent);

                    Intent menuactivity = new Intent(menu.this, Backup.class);

                    Bundle bndlanimation =
                            ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anni1, R.anim.anni2).toBundle();
                    startActivity(menuactivity, bndlanimation);



                } // end card 2 onClick
            }); // end card 2

            card3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(menu.this, "Button 3", Toast.LENGTH_LONG).show();
                    rebootcommand(); // REBOOT DEVICE

                } // end card  onClick
            }); // end card 3

        } // ends root else
    } // end oncreate

    public void launchforum() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://forums.bitsyko.com/index.php?sid=8dabaae5fd44b00df498fa84e36f6924")));
    }

    public void launchwebsite() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bitsyko.com/")));
    }

    public void launchxda() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://forum.xda-developers.com/android/apps-games/official-layers-bitsyko-apps-rro-t3012172")));
    }

    public void launchgplus() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/communities/102261717366580091389")));
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


}
