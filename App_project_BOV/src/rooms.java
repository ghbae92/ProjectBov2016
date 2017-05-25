package com.example.bov;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class rooms extends Activity {
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    BluetoothDevice device;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    int stat;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    TextView TextView3;
    TextView TextView4;
    TextView TextView5;
    Handler handler = new Handler();
    private TextToSpeech myTTS;
    EditText Height;
    private final int GOOGLE_STT = 1000, MY_UI = 1001;
    Intent i;
    private ArrayList<String> mResult1;                                    //�����ν� ��� ������ list
    private String mSelectedString1;
    String data;
    int j=0;
    StringBuffer sb1,sb2;
    String mag1,mag2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rooms);
        TextView3 = (TextView) findViewById(R.id.textView3);
        TextView4 = (TextView) findViewById(R.id.textView4);
        TextView5 = (TextView) findViewById(R.id.textView5);

        Height = (EditText) findViewById(R.id.editText);
        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                String speech = "���� �������� �����մϴ�.";
                myTTS.setLanguage(Locale.KOREA);
                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
       startActivityForResult(enableBluetooth, 1);

        handler.postDelayed(new Runnable() {
            //@Override
            public void run()
            {
                try {       findBT();
                    stat = 1;
                }
                catch (IOException e) {
                    stat = 0;
                    e.printStackTrace();
                }
            }
        }, 5000);
        final Button input = (Button) findViewById(R.id.height);
        input.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (stat == 1) {
                    try {
                        final String height = ((EditText)findViewById(R.id.editText)).getText().toString();
                        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int i) {
                                String speech = "������� ������ "+height+" ��ġ���� �Դϴ�";
                                myTTS.setLanguage(Locale.KOREA);
                                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        });
                        mmOutputStream.write(height.getBytes());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(rooms.this,
                                "Connection not established with your home",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else{
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();}
            }
        });

        final Button voice = (Button) findViewById(R.id.stt);
        voice.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        String speech = "���ϴ� �������� ������ �ּ���.";
                        myTTS.setLanguage(Locale.KOREA);
                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);            //intent ����
                        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());    //�����ν��� ȣ���� ��Ű��
                        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");                            //�����ν� ��� ����
                        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Control");
                        startActivityForResult(i, GOOGLE_STT);
                    }
                }, 3000);
            }
        });

        final Button light1 = (Button) findViewById(R.id.Button1);
        light1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (stat == 1) {
                    try {
                        mmOutputStream.write('1');
                        (new Handler()).postDelayed(new Runnable() {
                            public void run() {
                        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int i) {
                                String speech = "��ֹ� �ν��� �����մϴ�";
                                myTTS.setLanguage(Locale.KOREA);
                                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        });
                            }
                        }, 3000);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(rooms.this,
                                "Connection not established with your home",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else{
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();}
            }
        });


        final Button light2 = (Button) findViewById(R.id.Button2);
        light2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (stat == 1) {
                    try {
                        mmOutputStream.write('2');
                        (new Handler()).postDelayed(new Runnable() {
                            public void run() {
                                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        String speech = "��ֹ� �ν��� �����մϴ�.";
                                        myTTS.setLanguage(Locale.KOREA);
                                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                });
                            }
                        },3000);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(rooms.this,
                                "Connection not established with your home",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else{
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();}
            }
        });

        final Button light3 = (Button) findViewById(R.id.Button3);
        light3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (stat == 1) {
                    try {
                        mmOutputStream.write('3');
                        (new Handler()).postDelayed(new Runnable() {
                            public void run() {
                                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        String speech = "���ں�� �ν��� �����մϴ�.";
                                        myTTS.setLanguage(Locale.KOREA);
                                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                });
                            }
                        },3000);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(rooms.this,
                                "Connection not established with your home",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else{
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();}
            }
        });

        final Button light4 = (Button) findViewById(R.id.Button4);
        light4.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (stat == 1) {
                    try {
                        mmOutputStream.write('4');
                        (new Handler()).postDelayed(new Runnable() {
                            public void run() {
                                myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                    @Override
                                    public void onInit(int i) {
                                        String speech = "���ں�� �ν��� �����մϴ�.";
                                        myTTS.setLanguage(Locale.KOREA);
                                        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                    }
                                });
                            }
                        },3000);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(rooms.this,
                                "Connection not established with your home",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else{
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();}
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && (requestCode == GOOGLE_STT || requestCode == MY_UI)) {        //����� ������
            showSelectDialog(requestCode, data);                //����� ���̾�α׷� ���.
        } else {                                                            //����� ������ ���� �޽��� ���
            String msg = null;

            //���� ���� activity���� �Ѿ���� ���� �ڵ带 �з�
            switch (resultCode) {
                case SpeechRecognizer.ERROR_AUDIO:
                    msg = "����� �Է� �� ������ �߻��߽��ϴ�.";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    msg = "�ܸ����� ������ �߻��߽��ϴ�.";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    msg = "������ �����ϴ�.";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    msg = "��Ʈ��ũ ������ �߻��߽��ϴ�.";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    msg = "��ġ�ϴ� �׸��� �����ϴ�.";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    msg = "�����ν� ���񽺰� ������ �Ǿ����ϴ�.";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    msg = "�������� ������ �߻��߽��ϴ�.";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    msg = "�Է��� �����ϴ�.";
                    break;
            }

            if (msg != null)        //���� �޽����� null�� �ƴϸ� �޽��� ���
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void showSelectDialog(int requestCode, Intent data) {
        String key = "";
        if (requestCode == GOOGLE_STT)                    //���������ν��̸�
            key = RecognizerIntent.EXTRA_RESULTS;    //Ű�� ����
        else if (requestCode == MY_UI)                    //���� ���� activity �̸�
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            mResult1 = data.getStringArrayListExtra(key);        //�νĵ� ������ list �޾ƿ�.
            String[] result = new String[mResult1.size()];            //�迭����. ���̾�α׿��� ����ϱ� ����
            mResult1.toArray(result);                                    //	list �迭�� ��ȯ
            mSelectedString1 = result[0];
            Height.setText(mSelectedString1);
        final String num = ((EditText)findViewById(R.id.editText)).getText().toString();
        if(num.equals("��ֹ� �ν� ����")){
            if (stat == 1) {
                try {
                    mmOutputStream.write('1');
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int i) {
                                    String speech = "��ֹ� �ν��� �����մϴ�";
                                    myTTS.setLanguage(Locale.KOREA);
                                    myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            });
                        }
                    }, 3000);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else{
                Toast.makeText(rooms.this,
                        "Connection not established with your home",
                        Toast.LENGTH_LONG).show();}
        }
        if(num.equals("��ֹ� �ν� ����")){
            if (stat == 1) {
                try {
                    mmOutputStream.write('2');
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int i) {
                                    String speech = "��ֹ� �ν��� �����մϴ�";
                                    myTTS.setLanguage(Locale.KOREA);
                                    myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            });
                        }
                    }, 3000);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else{
                Toast.makeText(rooms.this,
                        "Connection not established with your home",
                        Toast.LENGTH_LONG).show();}
        }
        if(num.equals("���ں�� �ν� ����")){
            if (stat == 1) {
                try {
                    mmOutputStream.write('3');
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int i) {
                                    String speech = "���ں�� �ν��� �����մϴ�";
                                    myTTS.setLanguage(Locale.KOREA);
                                    myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            });
                        }
                    }, 3000);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else{
                Toast.makeText(rooms.this,
                        "Connection not established with your home",
                        Toast.LENGTH_LONG).show();}
        }
        if(num.equals("���ں�� �ν� ����")){
            if (stat == 1) {
                try {
                    mmOutputStream.write('4');
                    (new Handler()).postDelayed(new Runnable() {
                        public void run() {
                            myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int i) {
                                    String speech = "���ں�� �ν��� �����մϴ�";
                                    myTTS.setLanguage(Locale.KOREA);
                                    myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                }
                            });
                        }
                    }, 3000);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(rooms.this,
                            "Connection not established with your home",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            } else{
                Toast.makeText(rooms.this,
                        "Connection not established with your home",
                        Toast.LENGTH_LONG).show();}
        }
    }


    void findBT() throws IOException {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mmDevice = mBluetoothAdapter.getRemoteDevice("98:D3:31:70:9A:93");
        //mmDevice = mBluetoothAdapter.getRemoteDevice("20:16:05:06:22:46");

        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard //SerialPortService ID

        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();
        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                String speech = "������������ Ȱ��ȭ�ƽ��ϴ�";
                myTTS.setLanguage(Locale.KOREA);
                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        beginListenForData();
    }

    void beginListenForData() {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = mmInputStream.available();
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    sb1 = new StringBuffer();
                                    sb2 = new StringBuffer();
                                    if(data!=null){
                                        int len = data.length();
                                        for(int k =0 ; k<len ; k++){
                                            char c =data.charAt(k);
                                            if(c == '='){
                                                j++;
                                            }
                                            if(j%2 == 1){
                                                if((c >='0' && c<='9')){
                                                    sb1.append(c);
                                                }
                                            }
                                            if(j%2 == 0){
                                                if((c >='0' && c<='9')){
                                                    sb2.append(c);
                                                }
                                            }
                                        }
                                        mag1 = sb1.toString();
                                        mag2 = sb2.toString();
                                    }

                                    handler.post(new Runnable() {
                                        public void run() {
                                            TextView3.setText(data);
                                            TextView4.setText(sb1);
                                            TextView5.setText(sb2);
                                        }
                                    });

                                    int mag=Integer.parseInt(mag1);
                                    if( mag>=0 && mag<=60){
                                        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                            @Override
                                            public void onInit(int i) {
                                                String speech = "���� "+mag2+" ��ġ���� �տ� ���ں���� �ֽ��ϴ�.";
                                                myTTS.setLanguage(Locale.KOREA);
                                                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                            }
                                        });
                                    }
                                    if(mag >60 && mag<=120){
                                        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                            @Override
                                            public void onInit(int i) {
                                                String speech = "���� "+mag2+" ��ġ���� �տ� ���ں���� �ֽ��ϴ�.";
                                                myTTS.setLanguage(Locale.KOREA);
                                                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                            }
                                        });
                                    }
                                    if(mag >120 && mag<=180){
                                        myTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                            @Override
                                            public void onInit(int i) {
                                                String speech = "���� "+mag2+" ��ġ���� �տ� ���ں���� �ֽ��ϴ�.";
                                                myTTS.setLanguage(Locale.KOREA);
                                                myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                                            }
                                        });
                                    }
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        stopWorker = true;
                    }
                }
            }
        });
        workerThread.start();
    }


    /*@Override
    protected void onDestroy() {
        if (mmInputStream != null) {
            try {
                mmInputStream.close();
            } catch (Exception e) {
            }
            mmInputStream = null;
        }

        if (mmOutputStream != null) {
            try {
                mmOutputStream.close();
            } catch (Exception e) {
            }
            mmOutputStream = null;
        }

        if (mmSocket != null) {
            try {
                mmSocket.close();
            } catch (Exception e) {
            }
            mmSocket = null;
        }
        super.onDestroy();
        System.exit(0);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        MenuInflater h = getMenuInflater();
        h.inflate(R.menu.hardmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.abou:
                startActivity(new Intent("com.test.smarthome.ABOUT"));
                return true;
        }
        return false;
    }

}