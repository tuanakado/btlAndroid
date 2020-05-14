package com.example.truongle.btlandroid_appraoban.Chat.presenter;

import com.example.truongle.btlandroid_appraoban.Chat.model.Message;

/**
 * Created by truongle on 25/04/2017.
 */

public interface ChatLogicPresenter {
    public void send(Message mess, String from, String to);

}
