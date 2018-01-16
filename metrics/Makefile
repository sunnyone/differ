CC=g++
CFLAGS = `pkg-config --cflags opencv OpenCL`
LIBS = `pkg-config --libs opencv OpenCL`

SOURCES=metric.cpp core.cpp host_program_opencl.cpp similarityMetric.cpp mse.cpp psnr.cpp ssim.cpp msssim.cpp iqi.cpp mse_opencl.cpp ssim_opencl.cpp msssim_opencl.cpp iqi_opencl.cpp
OBJECTS=$(SOURCES:.cpp=.o)
EXECUTABLE=metric

all: $(SOURCES) $(EXECUTABLE)
		
$(EXECUTABLE): $(OBJECTS) 
		$(CC) -o $@ $(OBJECTS) $(LIBS)

$(OBJECTS): $(SOURCES)
		$(CC) $(CFLAGS)  -c $(SOURCES)

clean:
		rm -rf metric *.o

