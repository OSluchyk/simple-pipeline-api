package os.toolset.config;

public final class StageConfig extends BaseConfiguration{
    public static final String STG_NAME="name";
    public static final String STG_PROFILING_ENABLED="profiling";

    public StageConfig(Configuration conf) {
        super(conf);
    }

    public String name(){
        return getString(STG_NAME).required();
    }

    public boolean isProfilingEnabled(){
        return getBoolean(STG_PROFILING_ENABLED).orElse(false);
    }
}

