package com.wangfulin.dp.mediator;

/**
 * @projectName: desinepatterns
 * @description: 具体调停者类
 * 具体调停者知晓所有的具体同事类，
 * 并负责具体的协调各同事对象的交互关系。
 * @author: Wangfulin
 * @create: 2020-05-15 21:24
 **/
public class MainBoard implements Mediator {

    //需要知道要交互的同事类——光驱类
    private CDDriver cdDriver = null;
    //需要知道要交互的同事类——CPU类
    private CPU cpu = null;
    //需要知道要交互的同事类——显卡类
    private VideoCard videoCard = null;
    //需要知道要交互的同事类——声卡类
    private SoundCard soundCard = null;

    public CDDriver getCdDriver() {
        return cdDriver;
    }

    public void setCdDriver(CDDriver cdDriver) {
        this.cdDriver = cdDriver;
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public VideoCard getVideoCard() {
        return videoCard;
    }

    public void setVideoCard(VideoCard videoCard) {
        this.videoCard = videoCard;
    }

    public SoundCard getSoundCard() {
        return soundCard;
    }

    public void setSoundCard(SoundCard soundCard) {
        this.soundCard = soundCard;
    }

    @Override
    public void changed(Colleague c) {
        if (c instanceof CDDriver) {
            // 表示光驱读取数据了
            this.opeCDDriverReadData((CDDriver) c);
        } else if (c instanceof CPU) {
            // CPU解析数据
            this.opeCPU((CPU) c);
        }
    }

    /**
     * 处理光驱读取数据以后与其他对象的交互
     */
    private void opeCDDriverReadData(CDDriver cd) {
        //先获取光驱读取的数据
        String data = cd.getData();
        //把这些数据传递给CPU进行处理
        cpu.executeData(data);
    }


    /**
     * 处理CPU处理完数据后与其他对象的交互
     */
    private void opeCPU(CPU cpu) {
        //先获取CPU处理后的数据
        String videoData = cpu.getVideoData();
        String soundData = cpu.getSoundData();
        //把这些数据传递给显卡和声卡展示出来
        videoCard.showData(videoData);
        soundCard.soundData(soundData);
    }


}
