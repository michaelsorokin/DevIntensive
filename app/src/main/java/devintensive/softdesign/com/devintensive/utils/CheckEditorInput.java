package devintensive.softdesign.com.devintensive.utils;


import android.text.Editable;

import com.vicmikhailau.maskededittext.MaskedWatcher;

public class CheckEditorInput extends MaskedWatcher {

    public CheckEditorInput() {
        super(null);
    }

    @Override
    public void afterTextChanged(Editable s) {
        super.afterTextChanged(s);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
    }
}
