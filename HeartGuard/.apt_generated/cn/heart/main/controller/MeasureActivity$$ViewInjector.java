// Generated code from Butter Knife. Do not modify!
package cn.heart.main.controller;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class MeasureActivity$$ViewInjector<T extends cn.heart.main.controller.MeasureActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034175, "field 'mNick'");
    target.mNick = finder.castView(view, 2131034175, "field 'mNick'");
    view = finder.findRequiredView(source, 2131034174, "field 'mCircleHead'");
    target.mCircleHead = finder.castView(view, 2131034174, "field 'mCircleHead'");
    view = finder.findRequiredView(source, 2131034178, "field 'mSurfaceView'");
    target.mSurfaceView = finder.castView(view, 2131034178, "field 'mSurfaceView'");
    view = finder.findRequiredView(source, 2131034176, "field 'mRate'");
    target.mRate = finder.castView(view, 2131034176, "field 'mRate'");
  }

  @Override public void reset(T target) {
    target.mNick = null;
    target.mCircleHead = null;
    target.mSurfaceView = null;
    target.mRate = null;
  }
}
