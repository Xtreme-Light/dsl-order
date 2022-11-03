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
public class Rest implements Http {

  public To request(String uri) {
    return this.request(URI.create(uri));
  }

  public To request(URI uri) {
    return null;
  }
}
