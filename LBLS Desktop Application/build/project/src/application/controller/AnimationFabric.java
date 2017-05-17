package application.controller;

import java.util.LinkedList;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationFabric {
	public static  FadeTransition createOpacityAnim(Node node, double startVal, double finalVal, Duration durationVal) {
		FadeTransition opacityAnim = new FadeTransition(durationVal, (Node) node);
		opacityAnim.setFromValue(startVal);
		opacityAnim.setToValue(finalVal);
		return opacityAnim;
	}

	public static Timeline createPositionAnim(LinkedList<Node> nodeList, int startPos, int endPos) {
		Timeline animTimeline = new Timeline();

		for (int i = 0; i < nodeList.size(); ++i) {
			animTimeline.getKeyFrames().addAll(new KeyFrame[] {
					new KeyFrame(Duration.ZERO,
							new KeyValue[] { new KeyValue(((Node) nodeList.get(i)).translateYProperty(),
									Integer.valueOf(startPos)) }),
					new KeyFrame(Duration.millis(100.0d), new KeyValue[] {
							new KeyValue(((Node) nodeList.get(i)).translateYProperty(), Integer.valueOf(endPos)) }) });
		}

		return animTimeline;
	}
}