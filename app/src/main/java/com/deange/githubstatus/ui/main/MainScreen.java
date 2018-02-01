package com.deange.githubstatus.ui.main;

import android.os.Parcelable;

import com.deange.githubstatus.R;
import com.deange.githubstatus.ui.flow.HubbubPath;
import com.deange.githubstatus.ui.scoping.SingleIn;
import com.deange.githubstatus.ui.scoping.WithComponent;

import dagger.Subcomponent;

import static com.deange.githubstatus.ui.flow.HubbubPathParceler.forInstance;

@WithComponent(MainScreen.Component.class)
public class MainScreen extends HubbubPath implements Parcelable {

  public static final MainScreen INSTANCE = new MainScreen();

  private MainScreen() {
  }

  @Override
  public int layoutId() {
    return R.layout.main_view;
  }

  public static final Creator<MainScreen> CREATOR = forInstance(INSTANCE);

  @Subcomponent
  @SingleIn(MainScreen.class)
  public interface Component {
    void inject(MainView view);
  }

}
