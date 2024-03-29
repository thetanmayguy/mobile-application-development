package com.example.counter;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonStart, buttonStop, butup, butdown;
    TextView counterValue;
    EditText initial;
    public int counter=0;
    public boolean running=false, up=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//method initializes the layout
        setContentView(R.layout.activity_main);
        buttonStart=(Button)findViewById(R.id.btn_start);//variables=objects in the layout with the IDs
        buttonStart.setOnClickListener(this);//interface
        buttonStop=(Button)findViewById(R.id.btn_stop);
        buttonStop.setOnClickListener(this); //current activity (which implements the OnClickListener interface) will handle the clicks.
        counterValue=(TextView)findViewById(R.id.txt_value);
        butup=(Button)findViewById(R.id.butup);
        butup.setOnClickListener(this);
        butdown=(Button)findViewById(R.id.butdown);
        butdown.setOnClickListener(this);
        initial=(EditText) findViewById(R.id.txt_value);
    }

    @Override
    public void onClick(View v) { //method handles the click events of the two buttons/View object as its parameter
        if(v.equals(buttonStart)){
            counterStart();
            buttonStart.setEnabled(false);
            buttonStop.setEnabled(true);
        } else if(v.equals(buttonStop)) {
            counterStop();
            buttonStart.setEnabled(true);
            buttonStop.setEnabled(false);
        } else if(v.equals(butup)) {
            upcount();
        } else if(v.equals(butdown)) {
            downcount();
        }

    }

    private void upcount(){
        counter=Integer.parseInt(initial.getText().toString());
        up=true;
    }

    private void downcount(){
        counter=Integer.parseInt(initial.getText().toString());
        up=false;
    }

    private void counterStop() {
        this.running=false;
        //buttonStart.setEnabled(true); //presumably to prevent the user from clicking the buttonStop multiple times and potentially causing errors in the application.
        //buttonStop.setEnabled(false);
    }

    private void counterStart() {
        running=true;
        System.out.println("Start ->"+Thread.currentThread().getName());
        new MyCounter().start(); //creates a new instance of the MyCounter class (which presumably extends the Thread class), starts the thread using the start()
        //buttonStart.setEnabled(false); //method can be used to enable or disable a button in the user interface.
        //buttonStop.setEnabled(true);

    }

    Handler handler = new Handler(Looper.getMainLooper()) //object receives the message and updates the counterValue TextView with the current value of the counter.
    { //Handler object, which is used to receive messages from a background thread and update the UI on the main thread.
        public void handleMessage(Message mes){
            counterValue.setText(String.valueOf(mes.what));
        }
    };


    class MyCounter extends Thread{ //new instance extends the Thread class, and starts it
        public void run()
        {
            System.out.println("MyCounter ->"+Thread.currentThread().getName());
            while(running){ //loop that increments the counter variable and sends an empty message to the Handler object every second
                if(up)
                    counter++;
                else
                    counter--;
                handler.sendEmptyMessage(counter);
                try{
                    Thread.sleep(1000);
                } catch(Exception e){}//Exception that may occur during the Thread.sleep() method call.

            }
        }
    }
}