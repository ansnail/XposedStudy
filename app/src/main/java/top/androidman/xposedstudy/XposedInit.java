package top.androidman.xposedstudy;

import android.os.Bundle;
import android.widget.TextView;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by yanjie on 2018-06-26.
 * Describe:
 */
public class XposedInit implements IXposedHookLoadPackage ,IXposedHookZygoteInit ,IXposedHookInitPackageResources {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        //1.判断应用的包名
        if (lpparam.packageName.equals("top.androidman.xposedstudy")){
            //2.找到对应的类并hook掉对应相应的方法
            //需要注意findAndHookMethod方法的参数：
            //(1): 要hook掉的类名:需要全路径
            //(2): classloader：
            //(3): 要hook掉的方法名:只需方法名
            //(4): 方法的参数:有几个写几个
            //(5): XC_MethodHook,实现具体的方法，
            // beforeHookedMethod为hook前做的一些操作，afterHookedMethod为hook后做的一些操作

            XposedHelpers.findAndHookMethod("top.androidman.xposedstudy.MainActivity",
                    lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Class clazz = lpparam.classLoader.loadClass("top.androidman.xposedstudy.MainActivity");
                            Field field = clazz.getDeclaredField("text");
                            field.setAccessible(true);
                            XposedBridge.log("MainActivity");
                            TextView textView = (TextView) field.get(param.thisObject);
                            textView.setText("MainActivity");
                        }
                    });

        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {

    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        //hookLayout方法的参数：
        //1.包名：直接从参数中获取即可
        //2.资源的类型：有id，drawable，layout等等
        //3.布局名称：
        //4.回调接口
        resparam.res.hookLayout(resparam.packageName, "layout", "activity_main", new XC_LayoutInflated() {
            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {

            }
        });
    }
}
