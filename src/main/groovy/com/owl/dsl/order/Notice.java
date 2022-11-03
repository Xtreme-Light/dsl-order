package com.owl.dsl.order;

import java.net.URI;

/**
 * <p>
 *
 * </p>
 *
 * @author light
 * @since 2022-11-04
 **/
public interface Notice {

  default void uri(String uri){
    this.uri(URI.create(uri));
  }

  void uri(URI uri);
}
