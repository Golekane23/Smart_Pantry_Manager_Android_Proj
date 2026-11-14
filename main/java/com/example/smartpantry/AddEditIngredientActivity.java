package com.example.smartpantry;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {
    DatabaseHelper db; int id=-1; EditText name,quantity,unit,expiry;
    @Override protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_add_edit);db=new DatabaseHelper(this);
        name=findViewById(R.id.edtName);quantity=findViewById(R.id.edtQuantity);unit=findViewById(R.id.edtUnit);expiry=findViewById(R.id.edtExpiry);
        if(getIntent().hasExtra("id")){id=getIntent().getIntExtra("id",-1);name.setText(getIntent().getStringExtra("name"));quantity.setText(String.valueOf(getIntent().getDoubleExtra("quantity",0)));unit.setText(getIntent().getStringExtra("unit"));expiry.setText(getIntent().getStringExtra("expiry"));((TextView)findViewById(R.id.txtFormTitle)).setText("Edit ingredient");}
        findViewById(R.id.btnSave).setOnClickListener(v->save());
    }
    private void save(){
        String n=name.getText().toString().trim(), q=quantity.getText().toString().trim(), u=unit.getText().toString().trim(), e=expiry.getText().toString().trim();
        if(n.isEmpty()||q.isEmpty()||u.isEmpty()){Toast.makeText(this,"Enter ingredient name, quantity and unit.",Toast.LENGTH_LONG).show();return;}
        double amount; try{amount=Double.parseDouble(q);}catch(Exception ex){Toast.makeText(this,"Quantity must be a number.",Toast.LENGTH_LONG).show();return;}
        if(amount<=0){Toast.makeText(this,"Quantity must be greater than zero.",Toast.LENGTH_LONG).show();return;}
        if(id==-1) db.addIngredient(n,amount,u,e); else db.updateIngredient(id,n,amount,u,e);
        Toast.makeText(this,"Ingredient saved.",Toast.LENGTH_SHORT).show();finish();
    }
}
