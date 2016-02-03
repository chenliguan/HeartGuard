// Generated code from Butter Knife. Do not modify!
package cn.heart.main.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class PassSetActivity$$ViewInjector<T extends cn.heart.main.activity.PassSetActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034180, "field 'mNewPass'");
    target.mNewPass = finder.castView(view, 2131034180, "field 'mNewPass'");
    view = finder.findRequiredView(source, 2131034181, "field 'mOkNewPass'");
    target.mOkNewPass = finder.castView(view, 2131034181, "field 'mOkNewPass'");
    view = finder.findRequiredView(source, 2131034179, "field 'mOldPass'");
    target.mOldPass = finder.castView(view, 2131034179, "field 'mOldPass'");
  }

  @Override public void reset(T target) {
    target.mNewPass = null;
    target.mOkNewPass = null;
    target.mOldPass = null;
  }
}
