package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.TelephonyInfor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapter.TelephonyInforAdapter;

public class TelephonyActivity extends AppCompatActivity {
    ListView lvTelephony;
    TelephonyInforAdapter adapter;
    ArrayList<TelephonyInfor> allContacts;

    // Launcher for requesting permissions
    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_telephony);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the permission launcher
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
                    Boolean readContactsGranted = permissions.getOrDefault(Manifest.permission.READ_CONTACTS, false);
                    if (readContactsGranted) {
                        // Permission granted, load contacts
                        getAllContacts();
                    } else {
                        // Permission denied
                        Toast.makeText(this, "Read Contacts permission is required to display contacts.", Toast.LENGTH_SHORT).show();
                    }
                });

        addViews();
        // Check for permissions before trying to access contacts
        checkAndRequestPermissions();
        addEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.telephony_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_all) {
            filterContacts("all");
        } else if (itemId == R.id.menu_viettel) {
            filterContacts("viettel");
        } else if (itemId == R.id.menu_mobifone) {
            filterContacts("mobifone");
        } else if (itemId == R.id.menu_other) {
            filterContacts("other");
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterContacts(String network) {
        if (allContacts == null) return;

        adapter.clear();
        ArrayList<TelephonyInfor> filteredList = new ArrayList<>();

        List<String> viettelPrefixes = Arrays.asList("086", "096", "097", "098", "032", "033", "034", "035", "036", "037", "038", "039");
        List<String> mobifonePrefixes = Arrays.asList("089", "090", "093", "070", "079", "077", "076", "078");

        for (TelephonyInfor contact : allContacts) {
            String phone = contact.getPhone().replaceAll("\\s+", "");
            boolean isViettel = viettelPrefixes.stream().anyMatch(phone::startsWith);
            boolean isMobiFone = mobifonePrefixes.stream().anyMatch(phone::startsWith);

            if (network.equals("all")) {
                filteredList.add(contact);
            } else if (network.equals("viettel") && isViettel) {
                filteredList.add(contact);
            } else if (network.equals("mobifone") && isMobiFone) {
                filteredList.add(contact);
            } else if (network.equals("other") && !isViettel && !isMobiFone) {
                filteredList.add(contact);
            }
        }
        adapter.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }

    private void checkAndRequestPermissions() {
        String[] permissionsToRequest = {
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE
        };

        boolean allPermissionsGranted = true;
        for (String permission : permissionsToRequest) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (allPermissionsGranted) {
            // All permissions are already granted, load contacts
            getAllContacts();
        } else {
            // Request permissions
            requestPermissionLauncher.launch(permissionsToRequest);
        }
    }

    private void addEvents() {
        lvTelephony.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TelephonyInfor ti = adapter.getItem(i);
                makeAPhoneCall(ti);
            }
        });
    }

    private void makeAPhoneCall(TelephonyInfor ti) {
        Uri uri = Uri.parse("tel:" + ti.getPhone());
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(uri);
        // Check for CALL_PHONE permission before starting activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "CALL_PHONE permission not granted.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllContacts() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor == null) {
            Toast.makeText(this, "Could not query contacts.", Toast.LENGTH_SHORT).show();
            return;
        }

        allContacts.clear();
        adapter.clear();

        while (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            String name = cursor.getString(nameIndex); //Get Name
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String phone = cursor.getString(phoneIndex); //Get Phone Number

            TelephonyInfor ti = new TelephonyInfor();
            ti.setName(name);
            ti.setPhone(phone);
            allContacts.add(ti);
        }
        cursor.close();

        adapter.addAll(allContacts);
        adapter.notifyDataSetChanged();
    }

    private void addViews() {
        lvTelephony = findViewById(R.id.lvTelephonyInfor);
        adapter = new TelephonyInforAdapter(this, R.layout.item_telephony_infor);
        lvTelephony.setAdapter(adapter);
        allContacts = new ArrayList<>();
    }

    public void directCall(TelephonyInfor ti) {
        Uri uri = Uri.parse("tel:" + ti.getPhone());
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(uri);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "CALL_PHONE permission not granted.", Toast.LENGTH_SHORT).show();
        }
    }

    public void dialupCall(TelephonyInfor ti) {
        Uri uri = Uri.parse("tel:" + ti.getPhone());
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(uri);
        startActivity(intent);
    }
}