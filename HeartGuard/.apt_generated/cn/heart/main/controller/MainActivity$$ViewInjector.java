// Generated code from Butter Knife. Do not modify!
package cn.heart.main.controller;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class MainActivity$$ViewInjector<T extends cn.heart.main.controller.MainActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034171, "field 'sMainRelative' and method 'popupWindowClickListener'");
    target.sMainRelative = finder.castView(view, 2131034171, "field 'sMainRelative'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.popupWindowClickListener(p0);
        }
      });
  }

  @Override public void reset(T target) {
    target.sMainRelative = null;
  }
}
