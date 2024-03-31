package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontcontroller.v5.apdapter.ControllerV3HandlerAdapter;
import hello.servlet.web.frontcontroller.v5.apdapter.ControllerV4HandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", value = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private Map<String,Object> handlerMappingMap = new HashMap<>();
    private List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //  handlerMappingMap에서 요청에 맞는 컨트롤러 찾음
        Object handler = getHandler(request);

        // handlerAdapters에서 handler(컨트롤러)에 맞는 adapter를 찾음
        MyHandlerAdapter adapter = getAdapter(handler);

        // adapter의 컨트롤러가 로직을 처리할 수 있도록 데이터를 넘겨 줌
        // 컨트롤러에서 로직을 처리 후 ModelView의 인스턴스인 mv에 데이터를 넘겨줌 mv에는 viewPath에는 경로/ model에는 로직 처리 후 데이터가 들어있음
        ModelView mv = adapter.handle(request,response,handler);

        // viewPath의 단축경로 가져옴
        // viewPath의 단축경로를 viewResolver로 실제 경로로 전환 후 viewPath 필드에 실제 경로 값이 담긴 myView인스턴스가 생성
        String viewPath = mv.getViewPath();
        MyView myView =  viewResolver(viewPath);

        // myview 인스턴스에 컨트롤러로부터 데이터 로직이 처리된 모델을 넘김
        // 그 후에는 reder과정에서 model에서 request 파라미터로 데이터 전환 후 전환된 데이터로 JSP가 포워드 함
        myView.render(mv.getModel(),request,response);
    }

    private static MyView viewResolver(String viewName) {
        MyView myView = new MyView("/WEB-INF/views/"+ viewName +".jsp");
        return myView;
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        Object handler = handlerMappingMap.get(requestURI);
        return handler;
    }

    private MyHandlerAdapter getAdapter(Object handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if(adapter.supports(handler)){
                 return adapter;
            }
        } throw new IllegalArgumentException("hanlder를 지원하지 않습니다." + handler);
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form",new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save",new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members",new MemberListControllerV3());

        handlerMappingMap.put("/front-controller/v5/v4/members/new-form",new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save",new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members",new MemberListControllerV4());
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }


}
