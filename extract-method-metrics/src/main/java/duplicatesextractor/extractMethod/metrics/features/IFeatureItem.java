package duplicatesextractor.extractMethod.metrics.features;

public interface IFeatureItem {
    int getId();

    double getValue();

    void setValue(double newValue);
}