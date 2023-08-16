import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Subway {
    static int Vertex_num;
    static HashMap<Vertex, Integer> stationMap = new HashMap<>();
    static HashMap<String, Vertex> stringToVertexMap = new HashMap<>();
    static HashMap<String, ArrayList<String>> nameMap = new HashMap<>();
    static ArrayList<Vertex> vertexList = new ArrayList<>();
    static ArrayList<Edge> edgeList = new ArrayList<>();
    static ArrayList<Vertex> toList = new ArrayList<>();

    private static class Vertex {
        String name; // korean text
        String code;
        boolean defaultTransfer;
        ArrayList<Edge> adj;

        public Vertex(String name, String code, String line) {
            this.name = name;
            this.code = code;
            this.adj = new ArrayList<>();
            this.defaultTransfer = true;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class Edge implements Comparable<Edge> {
        Vertex end;
        int weight;

        public Edge(Vertex end, int weight) {
            this.end = end;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return this.weight - other.weight;
        }
    }

    private static class resultPair {
        ArrayList<Vertex> path;
        int duration;

        public resultPair() {
            this.duration = 0;
            this.path = new ArrayList<>();
        }
    }

    private static resultPair Dijkstra(Vertex start, Vertex End) {
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        int[] dist = new int[vertexList.size()];
        int[] parent = new int[vertexList.size()];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Stack<Vertex> pathStack = new Stack<>();
        ArrayList<Vertex> returnList = new ArrayList<>();
        Vertex realEnd = End;
        resultPair rPair = new resultPair();

        // Initializing 0 for the stations with same name
        for (String item : nameMap.get(start.name)) {
            Vertex v = stringToVertexMap.get(item);
            pq.add(new Edge(v, 0));
            dist[stationMap.get(v)] = 0;
            parent[stationMap.get(v)] = stationMap.get(v);
        }
        while (!pq.isEmpty()) {
            Edge curr = pq.poll();
            if (dist[stationMap.get(curr.end)] < curr.weight)
                continue;
            for (Edge nextPair : curr.end.adj) {
                int nextWeight = curr.weight + nextPair.weight;
                if (dist[stationMap.get(nextPair.end)] > nextWeight) {
                    dist[stationMap.get(nextPair.end)] = nextWeight;
                    parent[stationMap.get(nextPair.end)] = stationMap.get(curr.end);
                    pq.add(new Edge(nextPair.end, nextWeight));
                }
            }
        }
        for (String item : nameMap.get(End.name)) {
            Vertex v = stringToVertexMap.get(item);
            if (dist[stationMap.get(v)] < dist[stationMap.get(realEnd)])
                realEnd = v;
        }
        rPair.duration = dist[stationMap.get(realEnd)];
        while (parent[stationMap.get(realEnd)] != stationMap.get(realEnd)) {
            pathStack.push(realEnd);
            realEnd = vertexList.get(parent[stationMap.get(realEnd)]);
        }
        while (!pathStack.isEmpty()) {
            returnList.add(pathStack.pop());
        }
        rPair.path = returnList;
        return rPair;
    }

    public static void main(String args[]) {
        init(args[0]); // initializing the subway system
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String input = br.readLine();
                if (input.compareTo("QUIT") == 0)
                    break;

                command(input);
            } catch (IOException e) {
                System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
            }
        }
    }

    private static void init(String input) {
        int curr_mode = 0; // indicates information type of data
        Path path = Paths.get(input);
        if (!path.isAbsolute()) {
            path = Paths.get(System.getProperty("user.dir"), input);
        }
        try {
            FileReader fileReader = new FileReader(input);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    curr_mode++;
                    continue;
                }
                String[] words = line.split("\\s+");

                // Adding vertices to subway network
                if (curr_mode == 0) {
                    Vertex v = new Vertex(words[1], words[0], words[2]);
                    vertexList.add(v);
                    stationMap.put(v, vertexList.size() - 1);
                    stringToVertexMap.put(words[0], v);
                    if (nameMap.containsKey(v.name)) {
                        nameMap.get(v.name).add(words[0]);
                    } else {
                        ArrayList<String> temp = new ArrayList<>();
                        temp.add(v.code);
                        nameMap.put(v.name, temp);
                    }
                }

                // Adding edges to subway network
                else if (curr_mode == 1) {
                    Vertex start = stringToVertexMap.get(words[0]);
                    Vertex end = stringToVertexMap.get(words[1]);
                    int weight = Integer.parseInt(words[2]);
                    start.adj.add(new Edge(end, weight));
                }
                // Setting transfer time
                else if (curr_mode == 2) {
                    ArrayList<String> transfers = nameMap.get(words[0]);
                    int n = transfers.size();
                    for (int i = 0; i < n; i++) {
                        Vertex start = stringToVertexMap.get(transfers.get(i));
                        start.defaultTransfer = false;
                        for (int j = i + 1; j < n; j++) {
                            Vertex end = stringToVertexMap.get(transfers.get(j));
                            int weight = Integer.parseInt(words[1]);
                            start.adj.add(new Edge(end, weight));
                            end.adj.add(new Edge(start, weight));
                        }
                    }
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Setting default transfer time to 5
        for (Map.Entry<String, ArrayList<String>> entry : nameMap.entrySet()) {
            ArrayList<String> transfers = entry.getValue();
            int n = transfers.size();
            if (transfers.size() == 1 || stringToVertexMap.get(transfers.get(0)).defaultTransfer == false)
                continue;
            for (int i = 0; i < n; i++) {
                Vertex start = stringToVertexMap.get(transfers.get(i));
                for (int j = i + 1; j < n; j++) {
                    Vertex end = stringToVertexMap.get(transfers.get(j));
                    start.adj.add(new Edge(end, 5));
                    end.adj.add(new Edge(start, 5));
                }
            }
        }
    }

    private static void command(String input) {
        String[] words = input.split("\\s+");
        Vertex startVertex = stringToVertexMap.get(nameMap.get(words[0]).get(0));
        Vertex endVertex = stringToVertexMap.get(nameMap.get(words[1]).get(0));
        resultPair newPair = Dijkstra(startVertex, endVertex);
        int duration = newPair.duration;
        ArrayList<Vertex> path = newPair.path;
        int i = 0;
        System.out.print(startVertex);
        while (startVertex.name.equals(path.get(i).name))
            i++;
        for (; i < path.size(); i++) {
            if (i > 0 && path.get(i).name.equals(path.get(i - 1).name))
                continue;
            if (i < path.size() - 1 && path.get(i).name.equals(path.get(i + 1).name)) {
                System.out.print(" [" + path.get(i) + "]");
            } else
                System.out.print(" " + path.get(i));
        }
        System.out.print("\r\n");
        System.out.print(duration + "\r\n");
    }
}