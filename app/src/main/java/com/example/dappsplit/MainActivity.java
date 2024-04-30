package com.example.dappsplit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button CreateGroup;
    ListView listView;
    private CustomAdapter adapter;
    private ArrayList<GroupItem> groupItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CreateGroup = findViewById(R.id.btnGrp);

        CreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,GroupCreation.class);
                startActivity(intent);
            }
        });


        listView = findViewById(R.id.lvItems);
        adapter = new CustomAdapter(this, groupItems);
        listView.setAdapter(adapter);

        // Retrieve data from Firebase Realtime Database
        retrieveDataFromFirebase();
    }

    private void retrieveDataFromFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("groups");

            // Add a ValueEventListener to fetch data from the database
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    groupItems.clear(); // Clear the previous data

                    for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                        // Get the data for each group and create a GroupItem object
                        String grpKey = groupSnapshot.getKey();
                        String groupName = groupSnapshot.child("Groupname").getValue(String.class);
                        int amountSpilttedi = groupSnapshot.child("Amount").getValue(Integer.class);
                        int amountSettledi = groupSnapshot.child("AmounttoSettle").getValue(Integer.class);
                        int amountPayablei = groupSnapshot.child("payable").getValue(Integer.class);
                        String receipent = groupSnapshot.child("receipent").getValue(String.class);
                        String amountSpiltted = String.valueOf(amountSpilttedi);
                        String amountSettled = String.valueOf(amountSettledi);
                        String amountPayable = String.valueOf(amountPayablei);

                        GroupItem groupItem = new GroupItem(groupName, amountSpiltted, amountSettled, amountPayable,receipent,grpKey);
                        groupItems.add(groupItem);
                    }
                    adapter.notifyDataSetChanged(); // Notify the adapter of the data change
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }
}