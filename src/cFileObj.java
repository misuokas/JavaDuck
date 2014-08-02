import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class cFileObj {
    private static void parseVertex(String line, float[] vertex) {
        Scanner scanner = new Scanner(line);
        float w = 1.0f;

        if (true == scanner.hasNext() && 0 == scanner.next().compareTo("v")) {
            if (true == scanner.hasNext()) {
                vertex[0] = Float.valueOf(scanner.next());
            }
            if (true == scanner.hasNext()) {
                vertex[1] = Float.valueOf(scanner.next());
            }
            if (true == scanner.hasNext()) {
                vertex[2] = Float.valueOf(scanner.next());
            }
            if (true == scanner.hasNext()) {
                w = Float.valueOf(scanner.next());
            }

            vertex[0] = vertex[0] / w;
            vertex[1] = vertex[1] / w;
            vertex[2] = vertex[2] / w;
        }
        scanner.close();
    }

    private static void parseTextureVertex(String line, float[] vertex) {
        Scanner scanner = new Scanner(line);

        if (true == scanner.hasNext() && 0 == scanner.next().compareTo("vt")) {
            if (true == scanner.hasNext()) {
                vertex[0] = Float.valueOf(scanner.next());
            }
            if (true == scanner.hasNext()) {
                vertex[1] = Float.valueOf(scanner.next());
            }
            if (true == scanner.hasNext()) {
                vertex[2] = Float.valueOf(scanner.next());
            } else {
                vertex[2] = 0.0f;
            }
        }
        scanner.close();
    }

    private static void parseVertexNormal(String line, float[] vertex) {
        Scanner scanner = new Scanner(line);
        float w = 1.0f;

        if (true == scanner.hasNext() && 0 == scanner.next().compareTo("vn")) {
            if (true == scanner.hasNext()) {
                vertex[0] = Float.valueOf(scanner.next());
            }
            if (true == scanner.hasNext()) {
                vertex[1] = Float.valueOf(scanner.next());
            }
            if (true == scanner.hasNext()) {
                vertex[2] = Float.valueOf(scanner.next());
            }
            if (true == scanner.hasNext()) {
                w = Float.valueOf(scanner.next());
            }

            vertex[0] = vertex[0] / w;
            vertex[1] = vertex[1] / w;
            vertex[2] = vertex[2] / w;
        }
        scanner.close();
    }

    private static void parseFace(String line, int[] face) {
        Scanner scanner = new Scanner(line);

        if (true == scanner.hasNext() && 0 == scanner.next().compareTo("f")) {
            for (int i = 0; i < 3; i++) {
                if (true == scanner.hasNext()) {
                    String s[] = scanner.next().split("/");
                    face[i] = Integer.valueOf(s[0]) - 1;
                    if (2 == s.length) {
                        face[i + 3] = Integer.valueOf(s[1]) - 1;
                    } else if (3 == s.length) {
                        if (0 != s[1].length()) {
                            face[i + 3] = Integer.valueOf(s[1]) - 1;
                        }
                        face[i + 6] = Integer.valueOf(s[2]) - 1;
                    }
                }
            }
        }
        scanner.close();
    }

    private static boolean isVertexList(String line) {
        boolean returnValue = false;

        if (true == line.startsWith("v ")) {
            returnValue = true;
        }

        return returnValue;
    }

    private static boolean isTextureVertexList(String line) {
        boolean returnValue = false;

        if (true == line.startsWith("vt ")) {
            returnValue = true;
        }

        return returnValue;
    }

    private static boolean isVertexNormalList(String line) {
        boolean returnValue = false;

        if (true == line.startsWith("vn ")) {
            returnValue = true;
        }

        return returnValue;
    }

    private static boolean isFaceList(String line) {
        boolean returnValue = false;

        if (true == line.startsWith("f ")) {
            returnValue = true;
        }

        return returnValue;
    }

    private static cMesh parseObj(BufferedReader reader) {
        ArrayList<Float> vertexList = null;
        ArrayList<Float> textureVertexList = null;
        ArrayList<Float> vertexNormalList = null;
        ArrayList<Integer> faceList = null;
        String line = null;
        cMesh mesh = null;
        float[] vertex = new float[3];
        int[] face = new int[9];
        int vertices = 0;
        int textureVertices = 0;
        int vertexNormals = 0;
        int faces = 0;

        do {
            try {
                line = reader.readLine();

                if (null != line) {
                    if (true == isVertexList(line)) {
                        parseVertex(line, vertex);

                        if (null == vertexList) {
                            vertexList = new ArrayList<Float>();
                        }

                        vertexList.add(vertex[0]);
                        vertexList.add(vertex[1]);
                        vertexList.add(vertex[2]);

                        vertices++;
                    }

                    if (true == isTextureVertexList(line)) {
                        parseTextureVertex(line, vertex);

                        if (null == textureVertexList) {
                            textureVertexList = new ArrayList<Float>();
                        }

                        textureVertexList.add(vertex[0]);
                        textureVertexList.add(vertex[1]);
                        textureVertexList.add(vertex[2]);

                        textureVertices++;
                    }

                    if (true == isVertexNormalList(line)) {
                        parseVertexNormal(line, vertex);

                        if (null == vertexNormalList) {
                            vertexNormalList = new ArrayList<Float>();
                        }

                        vertexNormalList.add(vertex[0]);
                        vertexNormalList.add(vertex[1]);
                        vertexNormalList.add(vertex[2]);

                        vertexNormals++;
                    }

                    if (true == isFaceList(line)) {
                        parseFace(line, face);

                        if (null == faceList) {
                            faceList = new ArrayList<Integer>();
                        }

                        faceList.add(face[0]);
                        faceList.add(face[1]);
                        faceList.add(face[2]);
                        faceList.add(face[3]);
                        faceList.add(face[4]);
                        faceList.add(face[5]);
                        faceList.add(face[6]);
                        faceList.add(face[7]);
                        faceList.add(face[8]);

                        faces++;
                    }
                }
            } catch (IOException e) {
                System.out.println("cFileObj::parseObj( ): I/O error.");
            }
        } while (null != line);

        mesh = new cMesh();
        mesh.allocateVertices(vertices);
        if (0 != textureVertices) {
            mesh.allocateTextureVertices(textureVertices);
        }
        if (0 != vertexNormals) {
            mesh.allocateVertexNormals(vertexNormals);
        }
        mesh.allocateFaces(faces);

        for (int i = 0; i < vertices; i++) {
            mesh.vertex[i].setPosition(
                    vertexList.get(i * 3),
                    vertexList.get(i * 3 + 1),
                    vertexList.get(i * 3 + 2));
        }

        for (int i = 0; i < vertexNormals; i++) {
            mesh.vertexNormal[i].setPosition(
                    vertexNormalList.get(i * 3),
                    vertexNormalList.get(i * 3 + 1),
                    vertexNormalList.get(i * 3 + 2));
        }

        for (int i = 0; i < textureVertices; i++) {
            mesh.textureVertex[i].setPosition(
                    textureVertexList.get(i * 3),
                    textureVertexList.get(i * 3 + 1),
                    textureVertexList.get(i * 3 + 2));
        }

        for (int i = 0; i < faces; i++) {
            mesh.face[i].setVertices(
                    faceList.get(i * 9),
                    faceList.get(i * 9 + 1),
                    faceList.get(i * 9 + 2));

            mesh.face[i].setTextureVertices(
                    faceList.get(i * 9 + 3),
                    faceList.get(i * 9 + 4),
                    faceList.get(i * 9 + 5));

            mesh.face[i].setNormalVertices(
                    faceList.get(i * 9 + 6),
                    faceList.get(i * 9 + 7),
                    faceList.get(i * 9 + 8));
        }

        if (null != mesh) {
            mesh.debug();
        }

        return mesh;
    }

    private static BufferedReader getBufferedReader(String filename) {
        BufferedReader reader = null;

        if (true == filename.startsWith("http://") ||
                true == filename.startsWith("https://")) {
            try {
                URL url = new URL(filename);
                reader = new BufferedReader(
                        new InputStreamReader(url.openStream()));
            } catch (MalformedURLException e) {
                System.out.println(
                        "cFileObj::getBufferedReader( ): Malformed URL.");
            } catch (IOException e) {
                System.out.println(
                        "cFileObj::getBufferedReader( ): I/O error.");
            }
        } else {
            try {
                InputStream file = new FileInputStream(filename);
                reader = new BufferedReader(
                        new InputStreamReader(file));
            } catch (IOException e) {
                System.out.println(
                        "cFileObj::getBufferedReader( ): I/O error.");
            }
        }

        return reader;
    }

    public static cMesh load(String filename) {
        BufferedReader reader = null;
        cMesh mesh = null;

        reader = getBufferedReader(filename);

        if (null != reader) {
            mesh = parseObj(reader);
        }

        return mesh;
    }
}
