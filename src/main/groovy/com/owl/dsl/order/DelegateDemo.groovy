package com.owl.dsl.order

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * <p>
 *  示例版本
 * </p>
 *
 * @author light
 * @since 2022-11-03
 * */
class DelegateDemo {
    def orderProcess(@DelegatesTo(OrderProcessSpec) Closure cl) {
        def orderProcessSpec = new OrderProcessSpec()
        def code = cl.rehydrate(orderProcessSpec, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code.call()
    }

    static void main(String[] args) {
        def demo = new DelegateDemo()
        demo.orderProcess {
//            订单逻辑从状态A到状态B的过程，默认场景下，请求成功结束后状态变为B
            from StateA.A request "https://www.fangdaijisuanqi.com/" to StateA.B
            view
            from StateA.A request "https://www.fangdaijisuanqi.com/" then {
                route "200" to StateA.B
                route "400" to StateA.C
                route "500" request "error" to StateA.D
                route "401" request {
                    request "aaa" to StateA.A
                    request 'bbb' to StateA.B notice {
                        uri "https://www.fangdaijisuanqi.com/"
                    }
                }
            }
        }
    }
}

trait Code {
    Long getCode() {
        return 0L
    };
}

trait When {

    To request(String uriString) {
        URI.create(uriString)
        println "transform $uriString"
        return null
    }


}

trait End {
    void end(){

    }
    void notice(@DelegatesTo(Notice) Closure cl){
        def httpNotice = new HttpNotice()
        def code = cl.rehydrate(httpNotice, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code.call()
    }
}


trait To {

    End to(Code code) {
        return null
    }

    void then(@DelegatesTo(BoolCondition) Closure cl) {
        def boolCondition = new BoolCondition()
        def code = cl.rehydrate(boolCondition, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code.call()
    }

    To request(String body) {
        return null

    }

    void request(@DelegatesTo(Rest) Closure cl) {
        def rest = new Rest()
        def code = cl.rehydrate(rest, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code.call()
    }




}


enum StateA implements Code {
    A(1),
    B(2),
    C(3),
    D(4),
    ;
    private final Long code;

    StateA(Long code) {
        this.code = code
    }

    @Override
    Long getCode() {
        return this.code
    }
}

class OrderProcessContext {
    private Code sourceCode;
}

class OrderProcessSpec {
    private static final Logger log = LoggerFactory.getLogger(OrderProcessSpec.class)

    private OrderProcessContext context;

    def getView() {
        log.info "view $context"
        this
    }

    static When from(Code from) {
        println "from $from.code"
        return null
    }


}



