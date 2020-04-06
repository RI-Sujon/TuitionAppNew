package com.example.tuitionapp.CandidateTutor;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpT_Activity extends AppCompatActivity {
/*
    private EditText fName,lName,email,password,areaAddress,institute ;
    private DatabaseReference databaseReference ;
    private ProgressBar progressBar ;
    private FirebaseAuth mAuth;

    private String key,em ;
    private TutorAccountInfo AccountInfo ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_t);
        this.setTitle("SIGN UP");
        databaseReference = FirebaseDatabase.getInstance().getReference() ;
        mAuth = FirebaseAuth.getInstance() ;

    }

    public void signUpCompletion(View view){

        fName = findViewById(R.id.fName) ;
        lName = findViewById(R.id.lName) ;
        email = findViewById(R.id.email) ;
        password = findViewById(R.id.password) ;
        areaAddress = findViewById(R.id.areaAddress) ;
        institute = findViewById(R.id.institute) ;

        progressBar = findViewById(R.id.progressBar) ;
        progressBar.setVisibility(View.VISIBLE);

        String fN = fName.getText().toString().trim() ;
        String lN = lName.getText().toString().trim() ;
        String em = email.getText().toString().trim() ;
        String pas = password.getText().toString().trim() ;
        String aA = areaAddress.getText().toString().trim() ;
        String i = institute.getText().toString().trim() ;


        AccountInfo = new TutorAccountInfo(fN,lN,em,aA,i) ;

        key = databaseReference.push().getKey() ;

        mAuth.createUserWithEmailAndPassword(em,pas).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    databaseReference.child(key).setValue(AccountInfo) ;
                    Toast.makeText(getApplicationContext(),"sign up successfully",Toast.LENGTH_SHORT).show();
                    goToProfile();

                } else {
                    Toast.makeText(getApplicationContext(),"something wrong, try again" ,Toast.LENGTH_SHORT).show() ;
                }
            }
        });
    }

    public void goToProfile(){
        Intent intent = new Intent(this,ProfileT_Activity.class);
        startActivity(intent);
        finish();
    }

    public void backFromSignUpT_Activity(View view){
        Intent intent = new Intent(this, TutorModuleStart_Activity.class);
        startActivity(intent);
        finish();
    }

     public void addTuitionInfoToProfile(){
        System.out.println("Geche");
        medium.setText("Medium  :   " + tutorTuitionInfo.getMedium() );
        preferredClass.setText("Preferred Class  :   " + tutorTuitionInfo.getPreferredClass() );
        preferredGroup.setText("Preferred Group  :   " + tutorTuitionInfo.getPreferredGroup() );
        preferredSubject.setText("Preferred Subject  :   " + tutorTuitionInfo.getPreferredSubject() );
        daysPerWeekOrMonth.setText("Days Per Week  :   " + tutorTuitionInfo.getDaysPerWeekOrMonth() );
        salaryUpto.setText("Salary Upto  :   " + tutorTuitionInfo.getSalaryUpto() );
    }


*/
}

