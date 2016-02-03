// Generated code from Butter Knife. Do not modify!
package cn.heart.service.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class GridAdapter$ItemViewCache$$ViewInjector<T extends cn.heart.service.adapter.GridAdapter.ItemViewCache> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034276, "field 'mImageHead'");
    target.mImageHead = finder.castView(view, 2131034276, "field 'mImageHead'");
    view = finder.findRequiredView(source, 2131034278, "field 'mNick'");
    target.mNick = finder.castView(view, 2131034278, "field 'mNick'");
  }

  @Override public void reset(T target) {
    target.mImageHead = null;
    target.mNick = null;
  }
}
