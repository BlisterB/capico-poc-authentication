package com.excilys.capico_mock_authentication.service;

import com.excilys.capico_mock_authentication.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class BlackListedTokenService {
    private static final int MILLIS_BETWEEN_BLACKLIST_CLEANING = 3 * 1000 * 60; // 3 hours

    private static Thread cleaningBlackListTaskThread = null;
    @Autowired
    JwtUtil jwtUtil;

    private static Set<String> forbiddenTokens = new HashSet<>();

    public BlackListedTokenService(){
    }

    public boolean isBlackListed(String token){
        return forbiddenTokens.contains(token);
    }

    public void blackList(String token) {
        forbiddenTokens.add(token);
    }

    public void startCleaningBlackListTask(){
        // Just one cleaning task
        if (cleaningBlackListTaskThread == null || cleaningBlackListTaskThread.isInterrupted()){
            synchronized (this) {
                if (cleaningBlackListTaskThread == null || cleaningBlackListTaskThread.isInterrupted()) {
                    cleaningBlackListTaskThread = new Thread(new CleaningTaskRunnable());
                    cleaningBlackListTaskThread.start();
                }
            }
        }
    }

    class CleaningTaskRunnable implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(MILLIS_BETWEEN_BLACKLIST_CLEANING);
            } catch ( Exception e ) { }


            for(String stringToken : forbiddenTokens) {
                try {
                    jwtUtil.parseToken(stringToken);
                } catch (Exception e) {
                    // If a problem occurs when parsing the token (notably if it's expired), we can un black list it
                    forbiddenTokens.remove(stringToken);
                }
            }
        }
    }
}
