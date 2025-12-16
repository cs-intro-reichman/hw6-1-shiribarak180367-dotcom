
import java.awt.Color;

/**
 * A library of image processing functions.
 */
public class Runigram {

    public static void main(String[] args) {

        //// Hide / change / add to the testing code below, as needed.
		
		// Tests the reading and printing of an image:	
		Color[][] tinypic = read("tinypic.ppm");
        print(tinypic);

        // Creates an image which will be the result of various 
        // image processing operations:
        Color[][] image;

        // Tests the horizontal flipping of an image:
        image = flippedHorizontally(tinypic);
        System.out.println();
        print(image);

    

    //// Write here whatever code you need in order to test your work.
		//// You can continue using the image array.
	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
        In in = new In(fileName);
        // Reads the file header, ignoring the first and the third lines.
        in.readString();
        int numCols = in.readInt();
        int numRows = in.readInt();
        in.readInt();
        Color[][] image = new Color[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int r = in.readInt();
                int g = in.readInt();
                int b = in.readInt();
                image[i][j] = new Color(r, g, b);
            }
        }
        return image;
    }

    // Prints the RGB values of a given color.
    private static void print(Color c) {
        System.out.print("(");
        System.out.printf("%3s,", c.getRed());   // Prints the red component
        System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s", c.getBlue());  // Prints the blue component
        System.out.print(")  ");
    }

    // Prints the pixels of the given image.
    // Each pixel is printed as a triplet of (r,g,b) values.
    // This function is used for debugging purposes.
    // For example, to check that some image processing function works correctly,
    // we can apply the function and then use this function to print the resulting image.
    private static void print(Color[][] image) {
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                print(image[i][j]);
            }
            System.out.println();
        }

    

    //// Notice that all you have to so is print every element (i,j) of the array using the print(Color) function.
	}
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
        int N = image.length;
        int M = image[0].length;
        Color[][] newImage = new Color[M][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                newImage[i][j] = image[i][M - 1 - j];
            }
        }
        return newImage;
    }

    /**
     * Returns an image which is the vertically flipped version of the given
     * image.
     */
    public static Color[][] flippedVertically(Color[][] image) {
        int N = image.length;
        int M = image[0].length;
        Color[][] newImage2 = new Color[M][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                newImage2[i][j] = image[N - 1 - i][j];
            }
        }

        return newImage2;
    }

    // Computes the luminance of the RGB values of the given pixel, using the formula 
    // lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
    // the three values r = lum, g = lum, b = lum.
    private static Color luminance(Color pixel) {

        int r = pixel.getRed();
        int g = pixel.getGreen();
        int b = pixel.getBlue();
        int lum = (int) (0.299 * r + 0.587 * g + 0.114 * b);

        return new Color(lum, lum, lum);
    }

    /**
     * Returns an image which is the grayscaled version of the given image.
     */
    public static Color[][] grayScaled(Color[][] image) {
        int N = image.length;
        int M = image[0].length;
        Color[][] greyColors = new Color[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {

                greyColors[i][j] = luminance(image[i][j]);
            }
        }

        return greyColors;
    }

    /**
     * Returns an image which is the scaled version of the given image. The
     * image is scaled (resized) to have the given width and height.
     */
    public static Color[][] scaled(Color[][] image, int width, int height) {
        int H = image.length;
        int W = image[0].length;

        Color[][] newSize = new Color[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int I = (i * H) / height;
                int J = (j * W) / width;
                newSize[i][j] = image[I][J];
            }
        }
        return newSize;

    }

    /**
     * Computes and returns a blended color which is a linear combination of the
     * two given colors. Each r, g, b, value v in the returned color is
     * calculated using the formula v = alpha * v1 + (1 - alpha) * v2, where v1
     * and v2 are the corresponding r, g, b values in the two input color.
     */
    public static Color blend(Color c1, Color c2, double alpha) {
        int r = (int) (alpha * c1.getRed() + (1 - alpha) * c2.getRed());
        int g = (int) (alpha * c1.getGreen() + (1 - alpha) * c2.getGreen());
        int b = (int) (alpha * c1.getBlue() + (1 - alpha) * c2.getBlue());

        return new Color(r, g, b);
    }

    /**
     * Cosntructs and returns an image which is the blending of the two given
     * images. The blended image is the linear combination of (alpha) part of
     * the first image and (1 - alpha) part the second image. The two images
     * must have the same dimensions.
     */
    public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
        int N = image1.length;
        int M = image1[0].length;
        Color[][] blendColors = new Color[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                Color pixel1 = image1[i][j];
                Color pixel2 = image2[i][j];
                blendColors[i][j] = blend(pixel1, pixel2, alpha);
            }
        }

        return blendColors;
    }

    /**
     * Morphs the source image into the target image, gradually, in n steps.
     * Animates the morphing process by displaying the morphed image in each
     * step. Before starting the process, scales the target image to the
     * dimensions of the source image.
     */
    public static void morph(Color[][] source, Color[][] target, int n) {
        int H = source.length;
        int W = source[0].length;

        if (target.length != H || target[0].length != W) {
            target = scaled(target, W, H);
        }

        for (int i = 0; i <= n; i++) {
            double alpha = (n - i) / (double) n;
            Color[][] step = blend(source, target, alpha);
            display(step);
            StdDraw.pause(500);
        }
    }

    /**
     * Creates a canvas for the given image.
     */
    public static void setCanvas(Color[][] image) {
        StdDraw.setTitle("Runigram 2023");
        int height = image.length;
        int width = image[0].length;
        StdDraw.setCanvasSize(height, width);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
        // the StdDraw.show function is called.
        StdDraw.enableDoubleBuffering();
    }

    /**
     * Displays the given image on the current canvas.
     */
    public static void display(Color[][] image) {
        int height = image.length;
        int width = image[0].length;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Sets the pen color to the pixel color
                StdDraw.setPenColor(image[i][j].getRed(),
                        image[i][j].getGreen(),
                        image[i][j].getBlue());
                // Draws the pixel as a filled square of size 1
                StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
            }
        }
        StdDraw.show();
    }
}
