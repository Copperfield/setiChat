package es.uc3m.setichat.activity;

import es.uc3m.setichat.service.SeTIChatService;
import es.uc3m.setichat.service.SeTIChatServiceBinder;
import es.uc3m.setichat.utils.XMLParser;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

/**
 * This activity is use for dealing the sign up process.  
 * 
 * @author David Garcia Guerrero <miemailito@gmail.com>
 */

public class SeTIChatSignUpActivity extends Activity {

	private EditText nick,phone;

	private boolean DEBUG = false;

	private SeTIChatService mService;
	

	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
			// This is called when the connection with the service has been
			// established, giving us the service object we can use to
			// interact with the service. Because we have bound to a explicit
			// service that we know is running in our own process, we can
			// cast its IBinder to a concrete class and directly access it.
			mService = SeTIChatServiceBinder.getService();

			DEBUG = true;

			render();

		}

		public void onServiceDisconnected(ComponentName className) {
			// This is called when the connection with the service has been
			// unexpectedly disconnected -- that is, its process crashed.
			// Because it is running in our same process, we should never
			// see this happen.

			if (DEBUG)
				Log.d("SeTIChatSignUpActivity",
						"onServiceDisconnected: un-bounding the service");

			mService = null;
			Toast.makeText(SeTIChatSignUpActivity.this, "Disconnected", // R.string.local_service_disconnected,
					Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (mService == null) {
			// Binding the activity to the service to get shared objects
			if (DEBUG)
				Log.d("SeTIChatSignUpActivity", "Binding activity");
			bindService(new Intent(SeTIChatSignUpActivity.this,
					SeTIChatService.class), mConnection,
					Context.BIND_AUTO_CREATE);
		} else {
			render();
		}
		
		
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (DEBUG)
			Log.d("SeTIChatConversationActivity", "Unbinding activity");
		unbindService(mConnection);
	}
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	

	private void render() {
		// Tell the user about the service.
		Toast.makeText(SeTIChatSignUpActivity.this, "Connected", // R.string.local_service_connected,
				Toast.LENGTH_SHORT).show();

		int index = getIntent().getIntExtra("index", -1);
		if (DEBUG)
			Log.d("SeTIChatSignUpActivity",
					"onServiceConnected:" + index);
		setContentView(SignUpView(index));
	}


	public View SignUpView(int index) {

		// ***************************************************************** //
		// *********************** Layouts and Views *********************** //
		// ***************************************************************** //

		// Creating a general layout
		LinearLayout background = new LinearLayout(this);
		background.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		background.setOrientation(LinearLayout.VERTICAL);
		background.setPadding(0, 0, 0, 0);

		// Creating a layout for the edit text.
		LinearLayout background_edit = new LinearLayout(this);
		background_edit.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		background_edit.setOrientation(LinearLayout.VERTICAL);

		// Creating the edit text to add
		nick = new EditText(this);
		nick.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
		nick.setText("Type your nick here");
		phone = new EditText(this);
		phone.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
		phone.setText("Type your phone here");

		
		nick.requestFocus();

		// Of course a send button
		Button send = new Button(this);
		send.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 3f));
		send.setText("Sign in");

		// Sending messages
		send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String[] content = new String[2]; 
				
				if (DEBUG)
					Log.d("SeTIChatSignInActivity",
							"conversationView:OnClickListener: User clicked on sent button");
				
				// Parser to XML
				content[0] = nick.getText().toString();
				content[1] = phone.getText().toString();
				
				XMLParser xmlToSend = new XMLParser();
				String msg = xmlToSend.setRequest(
						content[1],
						"aplicación@appspot.com",
						"1",
						content);
				
				Log.d("SeTIChatSignInActivity",msg);
			
				mService.sendMessage(msg);
			}
		});

		
		// ***************************************************************** //
		// ******** Configuring the Views and returning the layout ******** //
		// ***************************************************************** //


		background_edit.addView(nick);
		background_edit.addView(phone);
		background_edit.addView(send);
		background.addView(background_edit);

		return background;
	}
	
	

}
