
#include <framework.h>
#include <string>
#include <opencv/cxcore.h>

class ImageSource
{
private:
    bool stop_pending;

    bool gray;
    std::string url;

    int mode; // 0 files, 1 video
    // mode dependent fields
    int imageIndex;
public:
    IplImage *currentImage;
    
public:
    Framework _framework;
    ImageSource();
    void initModule();
    void stopModule();
    void setUrl(char *url);
    void setGray(bool gray);
    void setStart(int imageIndex);
    void input();
    void skip();
};

CLASS_AS_MODULE(ImageSource);