package com.putri.esp;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.View;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.putri.esp.Overlay.getConfig;

public class ESPView extends View implements Runnable {
    Paint mStrokePaint;
    Paint mFilledPaint;
    Paint mTextPaint;
    Thread mThread;
    int FPS = 60;
    static long sleepTime;
    Date time;
    SimpleDateFormat formatter;
    public static void ChangeFps(int fps){
        sleepTime =1000/(20+fps);
    }

    public ESPView(Context context) {
        super(context, null, 0);
        InitializePaints();
        setFocusableInTouchMode(false);
        setBackgroundColor(Color.TRANSPARENT);
        time = new Date();
        formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        sleepTime = 1000 / FPS;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas != null && getVisibility() == VISIBLE) {
            ClearCanvas(canvas);
            int height = canvas.getHeight();
            float f = (height - 20);
            DrawText3(canvas, 255, 0, 255, 0, 1.0f, "Putz:- @putrimod", 180.0f, f, 20.0f);         
            Overlay.DrawOn(this, canvas);
        }
    }


    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        while (mThread.isAlive() && !mThread.isInterrupted()) {
            try {
                long t1 = System.currentTimeMillis();
                postInvalidate();
                long td = System.currentTimeMillis() - t1;

                Thread.sleep(Math.max(Math.min(0, sleepTime - td), sleepTime));
            } catch (Exception e) {
                Thread.currentThread().interrupt();//preserve the message
                return;
                //System.out.println("----------------\n"+e.toString()+"\n----------------------");
            }
        }
    }

    public void InitializePaints() {
        mStrokePaint = new Paint();
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setColor(Color.rgb(0, 0, 0));

        mFilledPaint = new Paint();
        mFilledPaint.setStyle(Paint.Style.FILL);
        mFilledPaint.setAntiAlias(true);
        mFilledPaint.setColor(Color.rgb(0, 0, 0));

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.rgb(0, 0, 0));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setStrokeWidth(1.1f);
    }

    public void ClearCanvas(Canvas cvs) {
        cvs.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }
public void DrawText3(Canvas cvs, int a, int r, int g, int b,float stroke, String txt, float posX, float posY, float size) {
    mTextPaint.setColor(Color.rgb(r, g, b));
    mTextPaint.setAlpha(a);
    mTextPaint.setStrokeWidth(stroke);
    if (getRight() > 1920 || getBottom() > 1920)
    mTextPaint.setTextSize(4 + size);
    else if (getRight() == 1920 || getBottom() == 1920)
    mTextPaint.setTextSize(2 + size);
    else
    mTextPaint.setTextSize(size);

    cvs.drawText(txt, posX, posY, mTextPaint);
    }
    //射线
    public void DrawLine(Canvas cvs, int a, int r, int g, int b, float lineWidth, float fromX, float fromY, float toX, float toY) {
        mStrokePaint.setColor(Color.rgb(r, g, b));
        mStrokePaint.setAlpha(a);
        mStrokePaint.setStrokeWidth(lineWidth);
        cvs.drawLine(fromX, fromY, toX, toY, mStrokePaint);
    }
    public void DrawRect(Canvas cvs, int a, int r, int g, int b, float stroke, float x, float y, float width, float height) {
        mStrokePaint.setStrokeWidth(stroke);
        mStrokePaint.setColor(Color.rgb(r, g, b));
        mStrokePaint.setAlpha(a);
        cvs.drawRect(x, y,  width,  height, mStrokePaint);
    }
    public void DrawFilledRect(Canvas cvs, int a, int r, int g, int b, float x, float y, float width, float height) {
        mFilledPaint.setColor(Color.rgb(r, g, b));
        mFilledPaint.setAlpha(a);
        cvs.drawRect(x, y, width, height, mFilledPaint);
    }
    public void DrawFilledRect3(Canvas cvs, int a, int r, int g, int b, float x, float y, float width, float height) {
        mFilledPaint.setColor(Color.rgb(0,100,0));
        mFilledPaint.setAlpha(a);
        cvs.drawRect(x-74, y-24, width+50, height-1.0f, mFilledPaint);
    }
    public void DebugText(String s) {
        System.out.println(s);
    }

    public void DrawText(Canvas cvs, int a, int r, int g, int b, String txt, float posX, float posY, float size) {
        mTextPaint.setARGB(a,r, g, b);
        mTextPaint.setTextSize(size);
        cvs.drawText(txt, posX, posY, mTextPaint);
    }
    //武器
    public void DrawWeapon(Canvas cvs, int a, int r, int g, int b, int id,int ammo, float posX, float posY, float size) {
        mTextPaint.setARGB(a,r, g, b);
        mTextPaint.setTextSize(size);
        String wname=getWeapon(id);
        if(wname!=null)
            cvs.drawText(wname+": "+ammo, posX, posY, mTextPaint);
    }

   public void DrawName(Canvas cvs, int a, int r, int g, int b, String nametxt,int teamid, float posX, float posY, float size) {
        String[] namesp = nametxt.split(":");
        char[] nameint = new char[namesp.length];
        for (int i = 0; i < namesp.length; i++)
            nameint[i] = (char) Integer.parseInt(namesp[i]);
        String realname = new String(nameint);        
        mTextPaint.setTextSize(20);
        mTextPaint.setColor(Color.rgb(255, 255, 255));
        cvs.drawText("" + realname, posX, posY+5, mTextPaint);
        mTextPaint.setTextSize(20);
        cvs.drawText("" + teamid, posX - 110.0f, posY+5, mTextPaint);
    }
    //物品
    public void DrawItems(Canvas cvs, String itemName,float distance, float posX, float posY, float size) {
        String realItemName = getItemName(itemName);
        mTextPaint.setTextSize(size);
        if(realItemName!=null && !realItemName.equals(""))
            cvs.drawText(realItemName+" ("+Math.round(distance)+"m)", posX, posY, mTextPaint);
    }
    //车辆
    public void DrawVehicles(Canvas cvs, String itemName,float distance, float posX, float posY, float size) {
        String realVehicleName = getVehicleName(itemName);
        mTextPaint.setColor(Color.YELLOW);
        mTextPaint.setAlpha(150);
        mTextPaint.setTextSize(size);
        if(realVehicleName!=null && !realVehicleName.equals(""))
            cvs.drawText(realVehicleName+": "+Math.round(distance)+"米", posX, posY, mTextPaint);
    }
    public void DrawCircle(Canvas cvs, int a, int r, int g, int b, float stroke, float posX, float posY, float radius) {
        mStrokePaint.setColor(Color.rgb(r, g, b));
        mStrokePaint.setAlpha(150);
        mStrokePaint.setStrokeWidth(stroke);
        cvs.drawCircle(posX + 100, posY +50, radius, mStrokePaint);
    }

    public void DrawFilledCircle(Canvas cvs, int a, int r, int g, int b, float posX, float posY, float radius) {
        mFilledPaint.setColor(Color.rgb(r, g, b));
        mFilledPaint.setAlpha(a);
        cvs.drawCircle(posX, posY, radius, mFilledPaint);
    }

    private String getItemName(String s) {
        //Scopes

        if (s.contains("MZJ_8X")&& getConfig("8x")) { mTextPaint.setARGB(255, 247, 99, 245);
            return "8x";
        }

        if (s.contains("MZJ_2X") && getConfig("2x")) { mTextPaint.setARGB(255, 230, 172, 226);
            return "2x";
        }

        if (s.contains("MZJ_HD") && getConfig("Red Dot")) { mTextPaint.setARGB(255, 230, 172, 226);
            return "Red Dot";
        }

        if (s.contains("MZJ_3X") && getConfig("3x")) { mTextPaint.setARGB(255, 247, 99, 245);
            return "3X";
        }

        if (s.contains("MZJ_QX") && getConfig("Hollow")) { mTextPaint.setARGB(255, 153, 75, 152);
            return "Hollow Sight";
        }

        if (s.contains("MZJ_6X") && getConfig("6x")) { mTextPaint.setARGB(255, 247, 99, 245);
            return "6x";
        }

        if (s.contains("MZJ_4X") && getConfig("4x")) { mTextPaint.setARGB(255, 247, 99, 245);
            return "4x";
        }

        if (s.contains("MZJ_SideRMR") && getConfig("Canted")) { mTextPaint.setARGB(255, 153, 75, 152);
            return "Canted Sight";
        }


        //AR and smg
        if (s.contains("AUG") && getConfig("AUG")) { mTextPaint.setARGB(255, 52, 224, 63);
            return "AUG";
        }

        if (s.contains("M762") && getConfig("M762")) { mTextPaint.setARGB(255, 43, 26, 28);
            return "M762";
        }

        if (s.contains("SCAR") && getConfig("SCAR-L")) { mTextPaint.setARGB(255, 52, 224, 63);
            return "SCAR-L";
        }

        if (s.contains("M416") && getConfig("M416")) { mTextPaint.setARGB(255, 115, 235, 223);
            return "M416";
        }

        if (s.contains("M16A4") && getConfig("M16A4")) { mTextPaint.setARGB(255, 116, 227, 123);
            return "M16A-4";
        }

        if (s.contains("Mk47") && getConfig("Mk47 Mutant")) { mTextPaint.setARGB(255, 247, 99, 245);
            return "Mk47 Mutant";
        }

        if (s.contains("G36") && getConfig("G36C")) { mTextPaint.setARGB(255, 116, 227, 123);
            return "G36C";
        }

        if (s.contains("QBZ") && getConfig("QBZ")) { mTextPaint.setARGB(255, 52, 224, 63);
            return "QBZ";
        }

        if (s.contains("AKM") && getConfig("AKM")) { mTextPaint.setARGB(255, 214, 99, 99);
            return "AKM";
        }

        if (s.contains("Groza") && getConfig("Groza")) { mTextPaint.setARGB(255, 214, 99, 99);
            return "Groza";
        }

        if (s.contains("PP19") && getConfig("Bizon")) { mTextPaint.setARGB(255, 255, 246, 0);
            return "Bizon";
        }

        if (s.contains("TommyGun") && getConfig("TommyGun")) { mTextPaint.setARGB(255, 207, 207, 207);
            return "TommyGun";
        }

        if (s.contains("MP5K") && getConfig("MP5K")) { mTextPaint.setARGB(255, 207, 207, 207);
            return "MP5K";
        }

        if (s.contains("UMP9") && getConfig("UMP")) { mTextPaint.setARGB(255, 207, 207, 207);
            return "UMP";
        }

        if (s.contains("Vector") && getConfig("Vector")) { mTextPaint.setARGB(255, 255, 246, 0);
            return "Vector";
        }

        if (s.contains("MachineGun_Uzi") && getConfig("Uzi")) { mTextPaint.setARGB(255, 255, 246, 0);
            return "Uzi";
        }

        if (s.contains("DP28") && getConfig("DP28")) { mTextPaint.setARGB(255, 43, 26, 28);
            return "DP28";
        }

        if (s.contains("M249") && getConfig("M249")) { mTextPaint.setARGB(255, 247, 99, 245);
            return "M249";
        }

        //snipers

        if (s.contains("AWM") && getConfig("AWM")) { mTextPaint.setColor(Color.BLACK);
            return "AWM";
        }

        if (s.contains("QBU") && getConfig("QBU")) { mTextPaint.setARGB(255, 207, 207, 207);
            return "QBU";
        }

        if (s.contains("SLR") && getConfig("SLR")) { mTextPaint.setARGB(255, 43, 26, 28);
            return "SLR";
        }

        if (s.contains("SKS") && getConfig("SKS")) { mTextPaint.setARGB(255, 43, 26, 28);
            return "SKS";
        }

        if (s.contains("Mini14") && getConfig("Mini14")) { mTextPaint.setARGB(255, 247, 99, 245);
            return "Mini14";
        }

        if (s.contains("M24") && getConfig("M24")) { mTextPaint.setARGB(255, 247, 99, 245);
            return "M24";
        }

        if (s.contains("Kar98k") && getConfig("Kar98k")) { mTextPaint.setARGB(255, 247, 99, 245);
            return "Kar98k";
        }

        if (s.contains("VSS") && getConfig("VSS")) { mTextPaint.setARGB(255, 255, 246, 0);
            return "VSS";
        }

        if (s.contains("Win94") && getConfig("Win94")) { mTextPaint.setARGB(255, 207, 207, 207);
            return "Win94";
        }

        if (s.contains("Mk14") && getConfig("Mk14")) { mTextPaint.setColor(Color.BLACK);
            return "Mk14";
        }

//shotguns and hand weapons
        if (s.contains("S12K") && getConfig("S12K")) { mTextPaint.setARGB(255, 153, 109, 109);
            return "S12K";
        }

        if (s.contains("DBS") && getConfig("DBS")) { mTextPaint.setARGB(255, 153, 109, 109);
            return "DBS";
        }

        if (s.contains("S686") && getConfig("S686")) { mTextPaint.setARGB(255, 153, 109, 109);
            return "S686";
        }

        if (s.contains("S1897") && getConfig("S1897")) { mTextPaint.setARGB(255, 153, 109, 109);
            return "S1897";
        }

        if (s.contains("Sickle") && getConfig("Sickle")) { mTextPaint.setARGB(255, 102, 74, 74);
            return "Sickle";
        }

        if (s.contains("Machete") && getConfig("Machete")) { mTextPaint.setARGB(255, 102, 74, 74);
            return "Machete";
        }

        if (s.contains("Cowbar") && getConfig("Cowbar")) { mTextPaint.setARGB(255, 102, 74, 74);
            return "Cowbar";
        }

        if (s.contains("CrossBow") && getConfig("CrossBow")) { mTextPaint.setARGB(255, 102, 74, 74);
            return "CrossBow";
        }

        if (s.contains("Pan") && getConfig("Pan")) { mTextPaint.setARGB(255, 102, 74, 74);
            return "Pan";
        }

        //pistols

        if (s.contains("SawedOff") && getConfig("SawedOff")) { mTextPaint.setARGB(255, 156, 113, 81);
            return "SawedOff";
        }

        if (s.contains("R1895") && getConfig("R1895")) { mTextPaint.setARGB(255, 156, 113, 81);
            return "R1895";
        }

        if (s.contains("Vz61") && getConfig("Vz61")) { mTextPaint.setARGB(255, 156, 113, 81);
            return "Vz61";
        }

        if (s.contains("P92") && getConfig("P92")) { mTextPaint.setARGB(255, 156, 113, 81);
            return "P92";
        }

        if (s.contains("P18C") && getConfig("P18C")) { mTextPaint.setARGB(255, 156, 113, 81);
            return "P18C";
        }

        if (s.contains("R45") && getConfig("R45")) { mTextPaint.setARGB(255, 156, 113, 81);
            return "R45";
        }

        if (s.contains("P1911") && getConfig("P1911")) { mTextPaint.setARGB(255, 156, 113, 81);
            return "P1911";
        }

        if (s.contains("DesertEagle") && getConfig("Desert Eagle")) { mTextPaint.setARGB(255, 156, 113, 81);
            return "DesertEagle";
        }


        //Ammo
        if (s.contains("Ammo_762mm") && getConfig("7.62")) { mTextPaint.setARGB(255, 92, 36, 28);
            return "7.62";
        }

        if (s.contains("Ammo_45AC") && getConfig("45ACP")) { mTextPaint.setColor(Color.LTGRAY);
            return "45ACP";
        }

        if (s.contains("Ammo_556mm") && getConfig("5.56")) { mTextPaint.setColor(Color.GREEN);
            return "5.56";
        }

        if (s.contains("Ammo_9mm") && getConfig("9mm")) { mTextPaint.setColor(Color.YELLOW);
            return "9mm";
        }

        if (s.contains("Ammo_300Magnum") && getConfig("300Magnum")) { mTextPaint.setColor(Color.BLACK);
            return "300Magnum";
        }

        if (s.contains("Ammo_12Guage") && getConfig("12 Guage")) { mTextPaint.setARGB(255, 156, 91, 81);
            return "12 Guage";
        }

        if (s.contains("Ammo_Bolt") && getConfig("Arrow")) { mTextPaint.setARGB(255, 156, 113, 81);
            return "Arrow";
        }

        //bag helmet vest
        if (s.contains("Bag_Lv3") && getConfig("Bag L 3")) { mTextPaint.setARGB(255, 36, 83, 255);
            return "Bag lvl 3";
        }

        if (s.contains("Bag_Lv1")  && getConfig("Bag L 1")) { mTextPaint.setARGB(255, 127, 154, 250);
            return "Bag lvl 1";
        }

        if (s.contains("Bag_Lv2") && getConfig("Bag L 2")) { mTextPaint.setARGB(255, 77, 115, 255);
            return "Bag lvl 2";
        }

        if (s.contains("Armor_Lv2") && getConfig("Vest L 2")) { mTextPaint.setARGB(255, 77, 115, 255);
            return "Vest lvl 2";
        }


        if (s.contains("Armor_Lv1") && getConfig("Vest L 1")) { mTextPaint.setARGB(255, 127, 154, 250);
            return "Vest lvl 1";
        }


        if (s.contains("Armor_Lv3") && getConfig("Vest L 3")) { mTextPaint.setARGB(255, 36, 83, 255);
            return "Vest lvl 3";
        }


        if (s.contains("Helmet_Lv2") && getConfig("Helmet 2")) { mTextPaint.setARGB(255, 77, 115, 255);
            return "Helmet lvl 2";
        }

        if (s.contains("Helmet_Lv1") && getConfig("Helmet 1")) { mTextPaint.setARGB(255, 127, 154, 250);
            return "Helmet lvl 1";
        }

        if (s.contains("Helmet_Lv3") && getConfig("Helmet 3")) { mTextPaint.setARGB(255, 36, 83, 255);
            return "Helmet lvl 3";
        }

        //Healthkits
        if (s.contains("Pills") && getConfig("PainKiller")) { mTextPaint.setARGB(255, 227, 91, 54);
            return "Painkiller";
        }

        if (s.contains("Injection") && getConfig("Adrenaline")) { mTextPaint.setARGB(255,204, 193, 190);
            return "Adrenaline";
        }

        if (s.contains("Drink") && getConfig("Energy Drink")) { mTextPaint.setARGB(255, 54, 175, 227);
            return "Energy Drink";
        }

        if (s.contains("Firstaid") && getConfig("FirstAidKit")) { mTextPaint.setARGB(255, 194, 188, 109);
            return "FirstAidKit";
        }

        if (s.contains("Bandage") && getConfig("Bandage")) { mTextPaint.setARGB(255, 43, 189, 48);
            return "Bandage";
        }

        if (s.contains("FirstAidbox") && getConfig("Medkit")) { mTextPaint.setARGB(255, 0, 171, 6);
            return "Medkit";
        }

        //Throwables
        if (s.contains("Grenade_Stun") && getConfig("Stung")) { mTextPaint.setARGB(255,204, 193, 190);
            return "Stung";
        }

        if (s.contains("Grenade_Shoulei") && getConfig("Grenade")) { mTextPaint.setARGB(255, 2, 77, 4);
            return "Grenade";
        }

        if (s.contains("Grenade_Smoke") && getConfig("Smoke")) { mTextPaint.setColor(Color.WHITE);
            return "Smoke";
        }

        if (s.contains("Grenade_Burn") && getConfig("Molotov")) { mTextPaint.setARGB(255, 230, 175, 64);
            return "Molotov";
        }


        //others
        if (s.contains("Large_FlashHider") && getConfig("Flash Hider Ar")) { mTextPaint.setARGB(255, 255, 213, 130);
            return "Flash Hider Ar";
        }

        if (s.contains("QK_Large_C") && getConfig("Ar Compensator")) { mTextPaint.setARGB(255, 255, 213, 130);
            return "Ar Compensator";
        }

        if (s.contains("Mid_FlashHider") && getConfig("Flash Hider SMG")) { mTextPaint.setARGB(255, 255, 213, 130);
            return "Flash Hider SMG";
        }

        if (s.contains("QT_A_") && getConfig("Tactical Stock")) { mTextPaint.setARGB(255, 158, 222, 195);
            return "Tactical Stock";
        }

        if (s.contains("DuckBill") && getConfig("Duckbill")) { mTextPaint.setARGB(255, 158, 222, 195);
            return "DuckBill";
        }

        if (s.contains("Sniper_FlashHider") && getConfig("Flash Hider Snp")) { mTextPaint.setARGB(255, 158, 222, 195);
            return "Flash Hider Sniper";
        }

        if (s.contains("Mid_Suppressor") && getConfig("Suppressor SMG")) { mTextPaint.setARGB(255, 158, 222, 195);
            return "Suppressor SMG";
        }

        if (s.contains("HalfGrip") && getConfig("Half Grip")) { mTextPaint.setARGB(255, 155, 189, 222);
            return "Half Grip";
        }


        if (s.contains("Choke") && getConfig("Choke")) { mTextPaint.setARGB(255, 155, 189, 222);
            return "Choke";
        }

        if (s.contains("QT_UZI") && getConfig("Stock Micro UZI")) { mTextPaint.setARGB(255, 155, 189, 222);
            return "Stock Micro UZI";
        }

        if (s.contains("QK_Sniper") && getConfig("SniperCompensator")) { mTextPaint.setARGB(255, 60, 127, 194);
            return "Sniper Compensator";
        }

        if (s.contains("Sniper_Suppressor") && getConfig("Sup Sniper")) { mTextPaint.setARGB(255, 60, 127, 194);
            return "Suppressor Sniper";
        }

        if (s.contains("Large_Suppressor") && getConfig("Suppressor Ar")) { mTextPaint.setARGB(255, 60, 127, 194);
            return "Suppressor Ar";
        }


        if (s.contains("Sniper_EQ_") && getConfig("Ex.Qd.Sniper")) { mTextPaint.setARGB(255, 193, 140, 222);
            return "Ex.Qd.Sniper";
        }

        if (s.contains("Mid_Q_") && getConfig("Qd.SMG")) { mTextPaint.setARGB(255, 193, 163, 209);
            return "Qd.SMG";
        }

        if (s.contains("Mid_E_") && getConfig("Ex.SMG")) { mTextPaint.setARGB(255, 193, 163, 209);
            return "Ex.SMG";
        }

        if (s.contains("Sniper_Q_") && getConfig("Qd.Sniper")) { mTextPaint.setARGB(255, 193, 163, 209);
            return "Qd.Sniper";
        }

        if (s.contains("Sniper_E_") && getConfig("Ex.Sniper")) { mTextPaint.setARGB(255, 193, 163, 209);
            return "Ex.Sniper";
        }

        if (s.contains("Large_E_") && getConfig("Ex.Ar")) { mTextPaint.setARGB(255, 193, 163, 209);
            return "Ex.Ar";
        }

        if (s.contains("Large_EQ_") && getConfig("Ex.Qd.Ar")) { mTextPaint.setARGB(255, 193, 140, 222);
            return "Ex.Qd.Ar";
        }

        if (s.contains("Large_Q_") && getConfig("Qd.Ar")) { mTextPaint.setARGB(255, 193, 163, 209);
            return "Qd.Ar";
        }

        if (s.contains("Mid_EQ_") && getConfig("Ex.Qd.SMG")) { mTextPaint.setARGB(255, 193, 140, 222);
            return "Ex.Qd.SMG";
        }

        if (s.contains("Crossbow_Q") && getConfig("Quiver CrossBow")) { mTextPaint.setARGB(255, 148, 121, 163);
            return "Quiver CrossBow";
        }

        if (s.contains("ZDD_Sniper") && getConfig("Bullet Loop")) { mTextPaint.setARGB(255, 148, 121, 163);
            return "Bullet Loop";
        }


        if (s.contains("ThumbGrip") && getConfig("Thumb Grip")) { mTextPaint.setARGB(255, 148, 121, 163);
            return "Thumb Grip";
        }

        if (s.contains("Lasersight") && getConfig("Laser Sight")) { mTextPaint.setARGB(255, 148, 121, 163);
            return "Laser Sight";
        }

        if (s.contains("Angled") && getConfig("Angled Grip")) { mTextPaint.setARGB(255, 219, 219, 219);
            return "Angled Grip";
        }

        if (s.contains("LightGrip") && getConfig("Light Grip")) { mTextPaint.setARGB(255, 219, 219, 219);
            return "Light Grip";
        }

        if (s.contains("Vertical") && getConfig("Vertical Grip")) { mTextPaint.setARGB(255, 219, 219, 219);
            return "Vertical Grip";
        }

        if (s.contains("GasCan") && getConfig("Gas Can")) { mTextPaint.setARGB(255, 255, 143, 203);
            return "Gas Can";
        }

        if (s.contains("Mid_Compensator") && getConfig("Compensator SMG")) { mTextPaint.setARGB(255, 219, 219, 219);
            return "Compensator SMG";
        }


        //special
        if (s.contains("Flare") && getConfig("Flare Gun")) { mTextPaint.setARGB(255, 242, 63, 159);
            return "Flare Gun";
        }

        if (s.contains("Ghillie") && getConfig("Ghillie Suit")) { mTextPaint.setARGB(255, 139, 247, 67);
            return "Ghillie Suit";
        }
        if (s.contains("CheekPad") && getConfig("CheekPad")) { mTextPaint.setARGB(255, 112, 55, 55);
            return "CheekPad";
        }
        if ( s.contains("PickUpListWrapperActor") && getConfig("Crate")) { mTextPaint.setARGB(255, 132, 201, 66);
            return "Crate";
        }
        if ((s.contains("AirDropPlane") )&& getConfig("DropPlane")) { mTextPaint.setARGB(255, 224, 177, 224);
            return "DropPlane";
        }
        if ((s.contains("AirDrop")  )&& getConfig("AirDrop")) { mTextPaint.setARGB(255, 255, 10, 255);
            return "AirDrop";
        }
        //return s;
        return null;

    }
    private String getWeapon(int id){
        if(id==101006)
            return "AUG";

        if(id==101008)
            return "M762" ;

        if(id==101003)
            return "SCAR-L";

        if(id==101004)
            return "M416";

        if(id==101002)
            return "M16A-4";

        if(id==101009)
            return "Mk47 Mutant";

        if(id==101010)
            return "G36C";

        if(id==101007)
            return "QBZ";

        if(id==101001)
            return "AKM";

        if(id==101005)
            return "Groza";

        if(id==102005)
            return "Bizon";

        if(id==102004)
            return "TommyGun";

        if(id==102007)
            return "MP5K";

        if(id==102002)
            return "UMP";

        if(id==102003)
            return "Vector";

        if(id==102001)
            return "Uzi";

        if(id==105002)
            return "DP28";

        if(id==105001)
            return "M249";

        //snipers

        if(id==103003)
            return "AWM";

        if(id==103010)
            return "QBU";

        if(id==103009)
            return "SLR";

        if(id==103004)
            return "SKS";

        if(id==103006)
            return "Mini14";

        if(id==103002)
            return "M24";

        if(id==103001)
            return "Kar98k";

        if(id==103005)
            return "VSS";

        if(id==103008)
            return "Win94";

        if(id==103007)
            return "Mk14";

//shotguns and hand weapons
        if(id==104003)
            return "S12K";

        if(id==104004)
            return "DBS";

        if(id==104001)
            return "S686";

        if(id==104002)
            return "S1897";

        if(id==108003)
            return "Sickle";

        if(id==108001)
            return "Machete";

        if(id==108002)
            return "Crowbar";

        if(id==107001)
            return "CrossBow";

        if(id==108004)
            return "Pan";

        //pistols

        if(id==106006)
            return "SawedOff";

        if(id==106003)
            return "R1895";

        if(id==106008)
            return "Vz61";

        if(id==106001)
            return "P92";

        if(id==106004)
            return "P18C";

        if(id==106005)
            return "R45";

        if(id==106002)
            return "P1911";

        if(id==106010)
            return "DesertEagle";

        return null;

    }
    private String getVehicleName(String s){
        if(s.contains("Buggy") && getConfig("Buggy"))
            return "Buggy";

        if(s.contains("UAZ") && getConfig("UAZ"))
            return "UAZ";

        if(s.contains("MotorcycleC") && getConfig("Trike") )
            return "Trike";

        if(s.contains("Motorcycle") && getConfig("Bike"))
            return "Bike";

        if(s.contains("Dacia") && getConfig("Dacia"))
            return "Dacia";

        if(s.contains("AquaRail") && getConfig("Jet"))
            return "Jet";

        if(s.contains("PG117") && getConfig("Boat"))
            return "Boat";

        if(s.contains("MiniBus") && getConfig("Bus"))
            return "Bus";

        if(s.contains("Mirado") && getConfig("Mirado"))
            return "Mirado";

        if(s.contains("Scooter") && getConfig("Scooter"))
            return "Scooter";

        if(s.contains("Rony") && getConfig("Rony"))
            return "Rony";

        if(s.contains("Snowbike") && getConfig("Snowbike"))
            return "Snowbike";

        if(s.contains("Snowmobile") && getConfig("Snowmobile"))
            return "Snowmobile";

        if(s.contains("Tuk") && getConfig("Tempo"))
            return "Tempo";

        if(s.contains("PickUp") && getConfig("Truck"))
            return "Truck";

        if(s.contains("BRDM") && getConfig("BRDM"))
            return "BRDM";

        if(s.contains("LadaNiva") && getConfig("LadaNiva"))
            return "LadaNiva";

        if(s.contains("Bigfoot") && getConfig("Monster Truck"))
            return "Monster Truck";

        return "";
    }
}
