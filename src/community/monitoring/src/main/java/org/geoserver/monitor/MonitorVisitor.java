package org.geoserver.monitor;

public interface MonitorVisitor<T> {

    void visit(T data, Object... aggregates);

}
