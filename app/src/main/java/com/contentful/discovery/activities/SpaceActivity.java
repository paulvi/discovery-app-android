package com.contentful.discovery.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import com.contentful.discovery.R;
import com.contentful.discovery.adapters.SpacePagerAdapter;
import com.contentful.discovery.api.CFClient;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.java.model.CDASpace;
import com.contentful.java.model.Locale;
import java.util.ArrayList;

/**
 * Space Activity.
 * Displays all available Content Types and Assets of a single Space in a tabbed layout.
 */
public class SpaceActivity extends AbsTabsActivity {
  private CDASpace space;
  private SpacePagerAdapter adapter;

  @Override protected void initOnCreate(Bundle savedInstanceState) {
    space = (CDASpace) getIntent().getSerializableExtra(IntentConsts.EXTRA_SPACE);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_space, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_locale:
        selectLocale();
        return true;

      case R.id.action_about:
        startActivity(new Intent(this, AboutActivity.class));
        return true;

      default:
    }

    return super.onOptionsItemSelected(item);
  }

  @Override protected String getTitleForActivity() {
    return space.getName();
  }

  @Override protected PagerAdapter getAdapter() {
    if (adapter == null) {
      adapter = new SpacePagerAdapter(this, getSupportFragmentManager());
    }

    return adapter;
  }

  /**
   * Displays an {@code AlertDialog}, populating it with a list of all available locales
   * from current {@code Space}. Upon selection, the new locale will be set using
   * {@link com.contentful.discovery.api.CFClient#setLocale(String)} method.
   */
  private void selectLocale() {
    ArrayList<String> list = new ArrayList<String>();

    // Construct a list of locales as strings
    for (Locale locale : CFClient.getClient().getSpace().getLocales()) {
      list.add(locale.code);
    }

    // Create an Adapter to be used with an AlertDialog
    final ArrayAdapter<String> adapter =
        new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, list);

    // Create an show an AlertDialog
    new AlertDialog.Builder(this).setTitle(getString(R.string.select_locale))
        .setAdapter(adapter, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            CFClient.setLocale(adapter.getItem(which));
          }
        })
        .show();
  }
}