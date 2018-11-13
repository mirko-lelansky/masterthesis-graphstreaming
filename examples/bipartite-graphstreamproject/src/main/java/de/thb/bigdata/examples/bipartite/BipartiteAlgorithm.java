/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package de.thb.bigdata.examples.bipartite;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.awt.Color;
import java.util.Iterator;
import java.util.Objects;

/**
 * This is class checks if an graph is bipartite or not.
 *
 * @author mlelansky
 */
public class BipartiteAlgorithm implements Algorithm {

    /**
     * The current graph to check.
     */
    private Graph graph;

    /**
     * The current bipartite state.
     */
    private boolean bipartite;

    /**
     * This is the first color to use.
     */
    private final Color firstColor;

    /**
     * This is the second color to use.
     */
    private final Color secondColor;

    /**
     * This is the default constructor.
     */
    public BipartiteAlgorithm() {
        this(Color.RED, Color.BLUE);
    }

    /**
     * This is the initialisation constructor.
     *
     * @param firstColor the first color
     * @param secondColor the second color
     */
    public BipartiteAlgorithm(final Color firstColor, final Color secondColor) {
        this.bipartite = true;
        this.firstColor = firstColor;
        this.secondColor = secondColor;
    }

    @Override
    public void init(final Graph graph) {
        this.graph = graph;
    }

    @Override
    public void compute() {
        Color[] coloredNodes = new Color[this.graph.getNodeCount()];
        colored(this.graph.getNode(0), this.firstColor, coloredNodes);
    }

    /**
     * This method colored the graph.
     *
     * @param root the root node
     * @param color the current color to use
     * @param result the marker array
     */
    private void colored(final Node root, final Color color, final Color[] result) {
        result[root.getIndex()] = color;
        Iterator<Node> nei = root.getNeighborNodeIterator();
        while (nei.hasNext()) {
            Node current = nei.next();
            if (Objects.isNull(result[current.getIndex()])) {
                if (color == this.firstColor) {
                    colored(current, this.secondColor, result);
                } else {
                    colored(current, this.firstColor, result);
                }
            } else {
                if (result[current.getIndex()] == color) {
                    this.bipartite = false;
                }
            }
        }
    }

    /**
     * This method returns true if the graph is bipartite.
     *
     * @return true if bipartite otherwise false
     */
    public boolean isBipartite() {
        return this.bipartite;
    }
}
