package hamitmizrak.com.familymessagingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class VkiActivity extends AppCompatActivity implements TextWatcher, SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener {
    //global variable
    private SeekBar seekBarVkiId;
    private RadioGroup radioGroupVkiId;
    private EditText editTextVki;
    private TextView textViewVkiKiloId;
    private TextView textViewVkiBoyId;
    private TextView textViewVkiDurumId;
    private TextView textViewVkiIdealId;

    //menu
    //Toolbar(Menu)
    private Toolbar myToolBarId;

    private double height = 0.0;
    private double weight = 70;
    private double idealWeightMale;
    private double idealWeightFemale;
    private double vki;

    //Bayan mı ?
    private boolean isFemale = true;

    //SeekBar
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        weight = i + 50;
        //Update
        updateHeightWeightGender();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    //RadioGroup
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == R.id.bayan) {
            isFemale = true;
        } else if (i == R.id.bay) {
            isFemale = false;
        }
        updateHeightWeightGender();
    }

    //TextWatcher
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        try {
            height = Double.valueOf(charSequence.toString()) / 100.0;
        } catch (NumberFormatException numberFormatException) {
            height = 0.0;
        }
        updateHeightWeightGender();
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vki);
        //start code

    /*    //Navbar id almak Toolbar id
        myToolBarId = findViewById(R.id.vkiToolBarId);
        myToolBarId.setTitle("Vki");
        myToolBarId.setLogo(R.drawable.logo);
        // myToolBarId.setNavigationIcon(R.drawable.logo);
        setSupportActionBar(myToolBarId);*/

        //id Almak
        seekBarVkiId = findViewById(R.id.seekBarVkiId);
        radioGroupVkiId = findViewById(R.id.radioGroupVkiId);
        editTextVki = findViewById(R.id.editTextVki);
        textViewVkiKiloId = findViewById(R.id.textViewVkiKiloId);
        textViewVkiBoyId = findViewById(R.id.textViewVkiBoyId);
        textViewVkiDurumId = findViewById(R.id.textVkiDurumId);
        textViewVkiIdealId = findViewById(R.id.textVkiIdealId);

        // interface
        //TextWatcher, SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener
        editTextVki.addTextChangedListener(this);
        seekBarVkiId.setOnSeekBarChangeListener(this);
        radioGroupVkiId.setOnCheckedChangeListener(this);

        updateHeightWeightGender();
    }//end onCreate

    //UPDATE
    private void updateHeightWeightGender() {
        textViewVkiBoyId.setText(String.valueOf(height) + " CM");
        textViewVkiKiloId.setText(String.valueOf(weight) + " KG");

        if(height>0){
            //erkek için ideal Formül
            idealWeightMale=(double)(50+2.3*(height*100*0.4-60));
            //bayan için ideal Formül
            idealWeightFemale=(double)(45.5+2.3*(height*100*0.4-60));
        }

        //VKI formülü
        vki=weight/(height*height);

        //Bayansa
        if (isFemale) {

            //bayan ise
            textViewVkiIdealId.setText(String.valueOf(idealWeightFemale));
            if (vki <= 19.1) {
                //zayıf
                textViewVkiDurumId.setBackgroundResource(R.color.zayif);
                textViewVkiDurumId.setText(R.string.zayif);
            } else if (19.1 < vki && vki <= 25.8) {
                //normal kilodan fazla
                textViewVkiDurumId.setBackgroundResource(R.color.normal);
                textViewVkiDurumId.setText(R.string.normal);
            } else if (25.8 < vki && vki <= 27.3) {
                //normal kilodan fazla
                textViewVkiDurumId.setBackgroundResource(R.color.normal_arti);
                textViewVkiDurumId.setText(R.string.normal_arti);
            } else if (27.3 < vki && vki <= 32.3) {
                //normal kilodan fazla
                textViewVkiDurumId.setBackgroundResource(R.color.fazla);
                textViewVkiDurumId.setText(R.string.fazla);
            } else if (32.3 < vki && vki <= 34.9) {
                //normal kilodan fazla
                textViewVkiDurumId.setBackgroundResource(R.color.obez);
                textViewVkiDurumId.setText(R.string.obez);
            } else {
                //normal kilodan fazla
                textViewVkiDurumId.setBackgroundResource(R.color.dikkat);
                textViewVkiDurumId.setText(R.string.dikkat);
            }

        } else {
            //erkek ise
            textViewVkiIdealId.setText(String.valueOf(idealWeightMale));
            if (vki <= 20.7) {
                //zayıf
                textViewVkiDurumId.setBackgroundResource(R.color.zayif);
                textViewVkiDurumId.setText(R.string.zayif);
            } else if (20.7 < vki && vki <= 26.4) {
                //normal kilodan fazla
                textViewVkiDurumId.setBackgroundResource(R.color.normal);
                textViewVkiDurumId.setText(R.string.normal);
            } else if (26.4 < vki && vki <= 27.8) {
                //normal kilodan fazla
                textViewVkiDurumId.setBackgroundResource(R.color.normal_arti);
                textViewVkiDurumId.setText(R.string.normal_arti);
            } else if (27.8 < vki && vki <= 31.1) {
                //normal kilodan fazla
                textViewVkiDurumId.setBackgroundResource(R.color.fazla);
                textViewVkiDurumId.setText(R.string.fazla);
            } else if (31.1 < vki && vki <= 34.9) {
                //normal kilodan fazla
                textViewVkiDurumId.setBackgroundResource(R.color.obez);
                textViewVkiDurumId.setText(R.string.obez);
            } else {
                //normal kilodan fazla
                textViewVkiDurumId.setBackgroundResource(R.color.dikkat);
                textViewVkiDurumId.setText(R.string.dikkat);
            }
        } //end else
    }

}//end VkiActivity