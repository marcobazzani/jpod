wget -c http://sourceforge.net/projects/jpodrenderer/files/dependencies/5.5/isFreetype.5.5.20120626.zip/download -O isFreetype.5.5.20120626.zip
wget -c http://sourceforge.net/projects/jpodrenderer/files/dependencies/5.5/isNative.5.5.20120626.zip/download -O isNative.5.5.20120626.zip
wget -c http://sourceforge.net/projects/jpodrenderer/files/dependencies/5.5/isCWT.5.5.20120626.zip/download -O isCWT.5.5.20120626.zip
wget -c http://sourceforge.net/projects/jpodrenderer/files/dependencies/5.5/jPod.5.5.20120626.zip/download -O jPod.5.5.20120626.zip
wget -c http://sourceforge.net/projects/jpodrenderer/files/dependencies/5.5/isrt.4.10.20120626.zip/download -O isrt.4.10.20120626.zip
wget -c http://www.jpedal.org/download/jbig2_src.zip

mkdir isFreetype
cd isFreetype
unzip ../isFreetype.*.zip
mv distribution/* .
cd ..

mkdir isNative
cd isNative
unzip ../isNative*.zip
mv distribution/* .
cd ..

mkdir isCWT
cd isCWT
unzip ../isCWT.*.zip
mv distribution/* .
cd ..

mkdir jPod
cd jPod
unzip ../jPod.*.zip
mv distribution/* .
cd ..

mkdir isrt
cd isrt
unzip ../isrt.*.zip
mv distribution/* .
cd ..

mkdir jpedal
cd jpedal
unzip ../jbig2_src.zip
cd ..

