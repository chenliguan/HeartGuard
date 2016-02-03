// Generated code from Butter Knife. Do not modify!
package cn.heart.service.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class UserListAdapter$ItemViewCache$$ViewInjector<T extends cn.heart.service.adapter.UserListAdapter.ItemViewCache> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034293, "field 'mRelativeFamily'");
    target.mRelativeFamily = finder.castView(view, 2131034293, "field 'mRelativeFamily'");
    view = finder.findRequiredView(source, 2131034295, "field 'mTextNick'");
    target.mTextNick = finder.castView(view, 2131034295, "field 'mTextNick'");
    view = finder.findRequiredView(source, 2131034294, "field 'mImageHead'");
    target.mImageHead = finder.castView(view, 2131034294, "field 'mImageHead'");
  }

  @Override public void reset(T target) {
    target.mRelativeFamily = null;
    target.mTextNick = null;
    target.mImageHead = null;
  }
}
