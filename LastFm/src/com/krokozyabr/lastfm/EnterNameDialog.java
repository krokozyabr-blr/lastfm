package com.krokozyabr.lastfm;

import com.krokozyabr.lastfm.networking.UrlManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class EnterNameDialog extends DialogFragment{

	private Context context;
	private SharedPreferences pref;
	
	private EnterNameListener nameEnteredListener;
	
	public interface EnterNameListener{
		void nameEntered(String name);
	}
	
	public void setOnNameEnteredListener(EnterNameListener nameEnteredListener) {
		this.nameEnteredListener = nameEnteredListener;
	}

	public EnterNameDialog(Context context){
		this.context = context;
	}
	
	@Override
	public  void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	};
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.enter_name_dialog, null);
		final Dialog dlg = getDialog();
		dlg.setTitle("Enter username");
		dlg.setCancelable(false);
		dlg.setCanceledOnTouchOutside(false);
		
		final EditText txtName = (EditText)view.findViewById(R.id.txtName);
		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
		final Button btnSave = (Button) view.findViewById(R.id.btnSave);
		
		btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!Utils.isEmpty(txtName.getText().toString())){
					pref = PreferenceManager.getDefaultSharedPreferences(context);
					pref.edit().putString(UrlManager.USERNAME_PREF, checkBox.isChecked() ? txtName.getText().toString() : "").commit();
				
					if (nameEnteredListener != null)
						nameEnteredListener.nameEntered(txtName.getText().toString());
					dlg.dismiss();
				} else {
					Toast.makeText(context, context.getResources().getString(R.string.enter_username), Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		return view;
	}
}
