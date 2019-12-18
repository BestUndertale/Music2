package com.example.music2;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicService {
    private static final File PATH = Environment.getExternalStorageDirectory();// 获取SD卡总目录。
    public List<String> musicList;// 存放找到的所有mp3的绝对路径。
    public MediaPlayer player; // 定义多媒体对象
    public int songNum; // 当前播放的歌曲在List中的下标,flag为标致
    public String songName; // 当前播放的歌曲名

    //加载MP3文件
    class MusicFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3"));//返回当前目录所有以.mp3结尾的文件
        }
    }

    //在MusicService类的无参构造函数中实例化对象，并把这些MP3文件放到musicList中。
    public MusicService() {
        super();
        player = new MediaPlayer();//实例化一个多媒体对象
        musicList = new ArrayList<String>();//实例化一个List链表数组
        try {
            File MUSIC_PATH = new File(PATH, "Music");//获取根目录的二级目录Music
            if (MUSIC_PATH.listFiles(new MusicFilter()).length > 0) {
                for (File file : MUSIC_PATH.listFiles(new MusicFilter())) {
                    musicList.add(file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            Log.i("TAG", "读取文件异常");
        }
    }

    //获取MP3文件名
    public void setPlayName(String dataSource) {
        File file = new File(dataSource);//假设为D:\\dd.mp3
        String name = file.getName();//name=dd.mp3
        int index = name.lastIndexOf(".");//找到最后一个 .
        songName = name.substring(0, index);//截取为dd
    }

    //播放
    public void play(){
        try{
            player.reset();
            String dataSource = musicList.get(songNum);//获取当前播放音乐的路径
            setPlayName(dataSource);//截取歌名
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(dataSource);//为多媒体对象设置播放路径
            player.prepare();
            player.start();
        }catch(Exception e){
            Log.v("MusicService", e.getMessage());
        }
    }

    //继续播放
    public void continueplay(){
        int position = getCurrentProgress();
        player.seekTo(position);//设置当前MediaPlayer的播放位置，单位是毫秒。
        try{
            player.prepare();
        }catch(Exception e){
            Log.v("MusicService", e.getMessage());
        }
        player.start();
    }

    //获取播放进度
    public int getCurrentProgress(){
        if(player != null & player.isPlaying()){
            return player.getCurrentPosition();
        }
        else if(player != null &(!player.isPlaying())){
            return player.getCurrentPosition();
        }
        return 0;
    }
    //随机播放
    public void random(){
        songNum = new Random().nextInt(musicList.size());
    }
    //下一首
    public void next(){
        songNum = songNum == musicList.size() - 1 ? 0 : songNum + 1;
        play();
    }

    //上一首
    public void last(){
        songNum = songNum == 0 ? musicList.size() - 1  : songNum - 1;
        play();
    }

    //暂停
    public void pause(){
        if(player != null & player.isPlaying()) player.pause();
    }

    //停止
    public void stop(){
        if(player != null & player.isPlaying()){
            player.stop();
            player.reset();
        }
    }
}







