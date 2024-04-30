package com.example.dappsplit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupCreation extends AppCompatActivity {


    EditText GrpName,Amount,Member;
    Button btnAddMem,btnCreateGroup;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_group_creation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        GrpName = findViewById(R.id.etGrpName);
        Amount = findViewById(R.id.etAmount);
        Member = findViewById(R.id.etAddMember);
        btnCreateGroup = findViewById(R.id.btnCreateGroup);
        btnAddMem = findViewById(R.id.btnAddMember);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        ArrayList<String> addressList = new ArrayList<>();
        btnAddMem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = Member.getText().toString().trim();
                if (!address.isEmpty()) {
                    addressList.add(address);
                    Member.getText().clear();
                    Toast.makeText(GroupCreation.this,"Member Added",Toast.LENGTH_SHORT);
                }
            }
        });


         btnCreateGroup.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String uid = mAuth.getCurrentUser().getUid();
                 DatabaseReference userRef = mDatabase.child("Users").child(uid);
                 DatabaseReference groupsRef = userRef.child("groups");

                 String groupKey = groupsRef.push().getKey();
                 String grpName = GrpName.getText().toString();
                 String amountstr = Amount.getText().toString();
                 int amount = Integer.parseInt(amountstr);
                 int peeps = addressList.size() + 1;
                 int split = amount / peeps;
                 int ams = amount - split;


                 groupsRef.child(groupKey).child("Groupname").setValue(grpName);
                 groupsRef.child(groupKey).child("AmounttoSettle").setValue(ams);
                 groupsRef.child(groupKey).child("payable").setValue(0);
                 groupsRef.child(groupKey).child("receipent").setValue(uid);
                 groupsRef.child(groupKey).child("Amount").setValue(amount);


                 for(String no : addressList){
                     DatabaseReference query = mDatabase.child("Link").child(no);
                     // Attach a ValueEventListener to the query
                     query.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             if (dataSnapshot.exists()) {

                                 String uuid = dataSnapshot.getValue(String.class);
                                 DatabaseReference dref= mDatabase.child("Users").child(uuid).child("groups").child(groupKey);
                                 dref.child("Groupname").setValue(grpName);
                                 dref.child("AmounttoSettle").setValue(ams);
                                 dref.child("payable").setValue(split);
                                 dref.child("Amount").setValue(amount);
                                 dref.child("receipent").setValue(uid);

                             } else {

                             }
                         }

                         @Override
                         public void onCancelled(DatabaseError databaseError) {
                             // Error handling
                         }
                     });
                 }
                 Intent intent = new Intent(GroupCreation.this,MainActivity.class);
                 startActivity(intent);
                 finish();
             }
         });

    }
}