package com.example.infoapp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.json.JSONObject;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ChatArrayAdapter chatArrayAdapter;
	private ListView listView;
	private EditText chatText;
	private Button buttonSend;
	private boolean side = true;

	String serverName = "192.168.1.37";
	int port = 8089;
	Socket client = null;
	DataOutputStream out = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		setContentView(R.layout.activity_main);
		try {
			client = new Socket(serverName, port);
			Toast.makeText(getApplicationContext(), "Connected to " + serverName + " on port " + port,
					Toast.LENGTH_SHORT).show();
			OutputStream outToServer = client.getOutputStream();
			out = new DataOutputStream(outToServer);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("mac_address", "test-mac-address");
			jsonObject.put("user_name", "arockiaraj");
			jsonObject.put("password", "test-password");
			jsonObject.put("mail", "test-mail-address");
			jsonObject.put("phone", "test-phone");
			byte[] data = ByteUtils.getInstance().serialize(jsonObject.toString());
			out.writeInt(data.length);
			out.write(data);
			out.flush();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		buttonSend = (Button) findViewById(R.id.send);
		listView = (ListView) findViewById(R.id.msgview);
		chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
		listView.setAdapter(chatArrayAdapter);
		chatText = (EditText) findViewById(R.id.msg);
		chatText.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					getMessageFromServer();
					return sendChatMessage();
				}
				return false;
			}

		});
		buttonSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getMessageFromServer();
				sendChatMessage();
			}
		});

		listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listView.setAdapter(chatArrayAdapter);

		// to scroll the list view to bottom on data change
		chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
				listView.setSelection(chatArrayAdapter.getCount() - 1);
			}
		});

	}

	private void getMessageFromServer() {
		// TODO Auto-generated method stub
		/*
		 * String dataFromServer = ""; try { DataInputStream in = new
		 * DataInputStream(client.getInputStream()); dataFromServer =
		 * in.readUTF(); } catch (IOException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 */
		chatArrayAdapter.add(new ChatMessage(!side, "Test Server "));
		// side = !side;
	}

	private boolean sendChatMessage() {
		try {
			byte[] data = ByteUtils.getInstance().serialize(chatText.getText().toString());
			out.writeInt(data.length);
			out.write(data);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
		chatText.setText("");
		// side = !side;
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
