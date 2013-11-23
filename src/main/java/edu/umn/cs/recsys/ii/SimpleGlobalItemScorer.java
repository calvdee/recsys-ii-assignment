package edu.umn.cs.recsys.ii;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import org.grouplens.lenskit.basic.AbstractGlobalItemScorer;
import org.grouplens.lenskit.scored.ScoredId;
import org.grouplens.lenskit.scored.ScoredIds;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Global item scorer to find similar items.
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class SimpleGlobalItemScorer extends AbstractGlobalItemScorer {
    private final SimpleItemItemModel model;

    @Inject
    public SimpleGlobalItemScorer(SimpleItemItemModel mod) {
        model = mod;
    }

    /**
     * Score items with respect to a set of reference items.
     * @param items The reference items.
     * @param scores The score vector. Its domain is the items to be scored, and the scores should
     *               be stored into this vector.
     */
    @Override
    public void globalScore(@Nonnull Collection<Long> items, @Nonnull MutableSparseVector scores) {
        scores.fill(0);

        // Score items in the domain of scores
        for (VectorEntry e : scores.fast()) {
            long item = e.getKey();

            double sum = 0;
            List<ScoredId> neighbors = model.getNeighbors(item);
            DoubleList scoresList = new DoubleArrayList();

            // Each item's score is the sum of its similarity to each item in items, if they are
            // neighbors in the model.

            for(ScoredId n : neighbors) {
                long id = n.getId();
                if(items.contains(id)) {
                    sum += n.getScore();
                }
            }


            scores.set(item, sum);
        }
    }
}
