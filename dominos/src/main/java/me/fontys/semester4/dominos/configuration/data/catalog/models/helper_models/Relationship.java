package me.fontys.semester4.dominos.configuration.data.catalog.models.helper_models;

import java.util.Objects;

public class Relationship<H, W>
{
    private final H husband;
    private final W wife;

    public Relationship(H husband, W wife) {
        this.husband = husband;
        this.wife = wife;
    }

    public H getLeft() {
        return husband;
    }

    public W getRight() {
        return wife;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Relationship)) return false;
        Relationship<?, ?> that = (Relationship<?, ?>) o;
        return husband.equals(that.husband) && wife.equals(that.wife);
    }

    @Override
    public int hashCode() {
        return Objects.hash(husband, wife);
    }
}
