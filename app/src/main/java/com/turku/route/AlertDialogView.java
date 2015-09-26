package com.turku.route;
import android.os.Bundle;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class AlertDialogView  extends DialogFragment{

	private int position;
	private int apostion;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {		
		Bundle bundle = getArguments();		
		position = bundle.getInt("position");
		apostion = bundle.getInt("aposition");
		AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
		
		if(position == 1){
			b.setTitle(AlertDialogueAdapter.Optimize_route);		
			b.setSingleChoiceItems(AlertDialogueAdapter.routeOptimize, apostion, null);
		}
		if(position == 3){
			b.setTitle(AlertDialogueAdapter.walking_speed);		
			b.setSingleChoiceItems(AlertDialogueAdapter.walkspeed, apostion, null);
		}
		if(position == 4){
			b.setTitle(AlertDialogueAdapter.numberofroute);	
			b.setSingleChoiceItems(AlertDialogueAdapter.routingNumber, apostion, null);
		}
		
		b.setPositiveButton(AlertDialogueAdapter.OK,positiveListener);
		AlertDialog d = b.create();
		return d;
	}
	
	OnClickListener positiveListener = new OnClickListener() {		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			AlertDialog alert = (AlertDialog)dialog;
			int pos = alert.getListView().getCheckedItemPosition();
			ChangeValue(position, pos);
		}
	};
	
	public static void ChangeValue(int position, int apostion){
		if(position == 1)
			switch(apostion){
			case 0: MainActivity.rOptimize = 1; break;
			case 1: MainActivity.rOptimize = 2; break;
			case 2: MainActivity.rOptimize = 3; break;
			case 3: MainActivity.rOptimize = 4; break;					
			}
		
		if(position == 3)
			switch(apostion){
			case 0: MainActivity.wSpeed = 1; break;
			case 1: MainActivity.wSpeed = 2; break;
			case 2: MainActivity.wSpeed = 3; break;
			case 3: MainActivity.wSpeed = 4; break;
			case 4: MainActivity.wSpeed = 5; break;					
			}
		
		if(position == 4)
			switch(apostion){
			case 0: MainActivity.rNumber = 2; break;
			case 1: MainActivity.rNumber = 3; break;
			case 2: MainActivity.rNumber = 5; break;					
			}
	}
}
