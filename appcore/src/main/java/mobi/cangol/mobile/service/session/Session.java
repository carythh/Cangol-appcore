package mobi.cangol.mobile.service.session;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import mobi.cangol.mobile.CoreApplication;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.service.AppService;
import mobi.cangol.mobile.service.conf.ConfigService;
import mobi.cangol.mobile.utils.FileUtils;
import mobi.cangol.mobile.utils.Object2FileUtils;
import mobi.cangol.mobile.utils.StringUtils;

/**
 * Created by xuewu.wei on 2018/5/2.
 */
public class Session {
    private static final String TAG = "session_";
    private static final String JSON = ".json";
    private static final String JSONA = ".jsona";
    private static final String SER = ".ser";
    private SharedPreferences mSharedPreferences = null;
    private Map<String, Object> mMap = new HashMap<>();
    private String mSessionDir;
    private String mName;
    private CoreApplication mCoreApplication;

    public Session(Context context, String name) {
        mName = name;
        mCoreApplication = (CoreApplication) context;
        mSharedPreferences = context.getSharedPreferences("session_" + name, Context.MODE_MULTI_PROCESS);
        final ConfigService configService = (ConfigService) mCoreApplication.getAppService(AppService.CONFIG_SERVICE);
        mSessionDir = configService.getCacheDir().getAbsolutePath() + File.separator + "session_" + name;

        final StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskReads();
        FileUtils.newFolder(mSessionDir);
        StrictMode.setThreadPolicy(oldPolicy);
    }

    public String getName() {
        return mName;
    }

    private SharedPreferences getShared() {
        return mSharedPreferences;
    }

    public boolean containsKey(String key) {
        return mMap.containsKey(key);
    }


    public boolean containsValue(Object value) {
        return mMap.containsValue(value);
    }

    public int getInt(String key, int defValue) {
        if (mMap.containsKey(key)) {
            return (int) mMap.get(key);
        }
        return defValue;
    }

    public boolean getBoolean(String key, boolean defValue) {
        if (mMap.containsKey(key)) {
            return (boolean) mMap.get(key);
        }
        return defValue;
    }

    public long getLong(String key, long defValue) {
        if (mMap.containsKey(key)) {
            return (long) mMap.get(key);
        }
        return defValue;
    }

    public float getFloat(String key, float defValue) {
        if (mMap.containsKey(key)) {
            return (float) mMap.get(key);
        }
        return defValue;
    }

    public String getString(String key, String defValue) {
        if (mMap.containsKey(key)) {
            return (String) mMap.get(key);
        }
        return defValue;
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        if (mMap.containsKey(key)) {
            return (Set<String>) mMap.get(key);
        }
        return defValue;
    }

    public JSONObject getJSONObject(String key) {
        if (mMap.containsKey(key)) {
            return (JSONObject) mMap.get(key);
        }
        return null;
    }

    public JSONArray getJSONArray(String key) {
        if (mMap.containsKey(key)) {
            return (JSONArray) mMap.get(key);
        }
        return null;
    }

    public Serializable getSerializable(String key) {
        if (mMap.containsKey(key)) {
            return (Serializable) mMap.get(key);
        }
        return null;
    }

    public void saveInt(String key, int value) {
        getShared().edit().putInt(key, value).apply();
        mMap.put(key, value);
    }

    public void saveBoolean(String key, boolean value) {
        getShared().edit().putBoolean(key, value).apply();
        mMap.put(key, value);
    }

    public void saveFloat(String key, float value) {
        getShared().edit().putFloat(key, value).apply();
        mMap.put(key, value);
    }

    public void saveLong(String key, long value) {
        getShared().edit().putLong(key, value).apply();
        mMap.put(key, value);
    }

    public void saveString(String key, String value) {
        getShared().edit().putString(key, value).apply();
        mMap.put(key, value);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void saveStringSet(String key, Set<String> value) {
        getShared().edit().putStringSet(key, value).apply();
        mMap.put(key, value);
    }

    public void saveJSONObject(final String key, final JSONObject value) {
        mMap.put(key, value);
        mCoreApplication.post(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG + mName, mSessionDir + File.separator + key + JSON);
                FileUtils.delete(mSessionDir + File.separator + key + JSON);
                Object2FileUtils.writeJSONObject2File(value, mSessionDir + File.separator + key + JSON);
                FileUtils.delete(mSessionDir + File.separator + key + JSONA);
                FileUtils.delete(mSessionDir + File.separator + key + SER);
            }
        });
    }

    public void saveJSONArray(final String key, final JSONArray value) {
        mMap.put(key, value);
        mCoreApplication.post(new Runnable() {
            @Override
            public void run() {
                FileUtils.delete(mSessionDir + File.separator + key + JSONA);
                Object2FileUtils.writeJSONArray2File(value, mSessionDir + File.separator + key + JSONA);
                FileUtils.delete(mSessionDir + File.separator + key + JSON);
                FileUtils.delete(mSessionDir + File.separator + key + SER);
            }
        });
    }

    public void saveSerializable(final String key, final Serializable value) {
        mMap.put(key, value);
        mCoreApplication.post(new Runnable() {
            @Override
            public void run() {
                FileUtils.delete(mSessionDir + File.separator + key + SER);
                Object2FileUtils.writeObject(value, mSessionDir + File.separator + key + SER);
                FileUtils.delete(mSessionDir + File.separator + key + JSON);
                FileUtils.delete(mSessionDir + File.separator + key + JSONA);
            }
        });
    }

    public void saveAll(Map<String, ?> map) {
        for (Map.Entry<String,?> entry: map.entrySet()) {
            if (entry.getValue() instanceof Float) {
                saveFloat(entry.getKey(), (Float) entry.getValue());
            } else if (entry.getValue() instanceof Boolean) {
                saveBoolean(entry.getKey(), (Boolean)entry.getValue());
            } else if (entry.getValue() instanceof String) {
                saveString(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                saveInt(entry.getKey(), (Integer) entry.getValue());
            } else if (entry.getValue() instanceof Long) {
                saveLong(entry.getKey(), (Long) entry.getValue());
            } else if (entry.getValue() instanceof Set) {
                saveStringSet(entry.getKey(), (Set<String>) entry.getValue());
            } else if (entry.getValue() instanceof JSONObject) {
                saveJSONObject(entry.getKey(), (JSONObject) entry.getValue());
            } else if (entry.getValue() instanceof JSONArray) {
                saveJSONArray(entry.getKey(), (JSONArray) entry.getValue());
            } else if (Serializable.class.isAssignableFrom(entry.getValue().getClass())) {
                saveSerializable(entry.getKey(), (Serializable)entry.getValue());
            } else {
                //其他缓存方案
                throw new IllegalArgumentException(entry.getValue().getClass() + " is not cache type");
            }
        }
    }

    public Object get(String key) {
        if (mMap.containsKey(key)) {
            return mMap.get(key);
        }
        return null;
    }

    public void put(String key, Object value) {
        mMap.put(key, value);
    }

    public void putAll(Map<String, ?> map) {
        mMap.putAll(map);
    }

    public void remove(final String key) {
        mMap.remove(key);
        getShared().edit().remove(key).apply();
        mCoreApplication.post(new Runnable() {
            @Override
            public void run() {
                FileUtils.delete(mSessionDir + File.separator + key + JSON);
                FileUtils.delete(mSessionDir + File.separator + key + JSONA);
                FileUtils.delete(mSessionDir + File.separator + key + SER);
            }
        });
    }

    public void clear() {
        mMap.clear();
    }

    public void clearAll() {
        mMap.clear();
        getShared().edit().clear().apply();
        mCoreApplication.post(new Runnable() {
            @Override
            public void run() {
                FileUtils.delAllFile(mSessionDir);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void refresh() {
        final StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskReads();
        final Map<String, ?> map = getShared().getAll();
        StrictMode.setThreadPolicy(oldPolicy);
        mMap.putAll(map);
        mCoreApplication.post(new Runnable() {
            @Override
            public void run() {
                mMap.putAll(loadDiskMap());
            }
        });
    }

    private Map<String, Object> loadDiskMap() {
        Log.d(TAG + mName, "scan cache file");
        final  List<File> files = FileUtils.searchBySuffix(new File(mSessionDir), null, JSON, JSONA, SER);
        Log.d(TAG + mName, "cache file=" + files);
        final Map<String, Object> map = new ConcurrentHashMap<>();
        for (final File file : files) {
            if (file.getName().endsWith(JSON)) {
                final JSONObject json = Object2FileUtils.readFile2JSONObject(file);
                final String key = file.getName().substring(0, file.getName().lastIndexOf(JSON));
                if (json != null && StringUtils.isNotBlank(key))
                    map.put(key, json);
            } else if (file.getName().endsWith(JSONA)) {
                final JSONArray jsona = Object2FileUtils.readFile2JSONArray(file);
                final String key = file.getName().substring(0, file.getName().lastIndexOf(JSONA));
                if (jsona != null && StringUtils.isNotBlank(key))
                    map.put(key, jsona);
            } else if (file.getName().endsWith(SER)) {
                final Object obj = Object2FileUtils.readObject(file);
                final String key = file.getName().substring(0, file.getName().lastIndexOf(SER));
                if (obj != null && StringUtils.isNotBlank(key))
                    map.put(key, obj);
            } else {
                //其他缓存方案
                Log.e("found cache file");
            }
        }
        return map;
    }
}
