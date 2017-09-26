#include <jni.h>

#include "include/bsdiff.c"
#include "include/bspatch.c"

extern "C"
JNIEXPORT jint JNICALL
Java_com_xiong_bsdiffdemo_util_Diffutils_generateDiffApk(JNIEnv *env, jclass type, jstring oldPath_,
                                               jstring newPath_, jstring patchPath_) {
    int argc = 4;
    char *argv[argc];
    argv[0] = (char *) "bspatch";
    argv[1] = (char *) env->GetStringUTFChars(oldPath_, 0);
    argv[2] = (char *) env->GetStringUTFChars(newPath_, 0);
    argv[3] = (char *) env->GetStringUTFChars(patchPath_, 0);

    jint result = generateDiffApk(argc, argv);

    env->ReleaseStringUTFChars(oldPath_, argv[1]);
    env->ReleaseStringUTFChars(newPath_, argv[2]);
    env->ReleaseStringUTFChars(patchPath_, argv[3]);
    return result;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_nick_bsdiff_Diffutils_mergeDiffApk(JNIEnv *env, jclass type, jstring oldPath_,
                                            jstring newPath_, jstring patchPath_) {

    int argc = 4;
    char *argv[argc];
    argv[0] = (char *) "bspatch";
    argv[1] = (char *) env->GetStringUTFChars(oldPath_, 0);
    argv[2] = (char *) env->GetStringUTFChars(newPath_, 0);
    argv[3] = (char *) env->GetStringUTFChars(patchPath_, 0);

    jint result = mergeDiffApk(argc, argv);

    env->ReleaseStringUTFChars(oldPath_, argv[1]);
    env->ReleaseStringUTFChars(newPath_, argv[2]);
    env->ReleaseStringUTFChars(patchPath_, argv[3]);
    return result;
}
