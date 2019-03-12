package groupEntity;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: Specification
 * @Description: 组合封装规格实体类
 * @Author: XuZhao
 * @CreateDate: 19/03/12$ 下午 09:35$
 */
public class Specification implements Serializable {

    private TbSpecification specification;

    private List<TbSpecificationOption> specificationOptions;

    public Specification(TbSpecification specification, List<TbSpecificationOption> specificationOptions) {
        this.specification = specification;
        this.specificationOptions = specificationOptions;
    }

    public Specification() {
    }

    public TbSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(TbSpecification specification) {
        this.specification = specification;
    }

    public List<TbSpecificationOption> getSpecificationOptions() {
        return specificationOptions;
    }

    public void setSpecificationOptions(List<TbSpecificationOption> specificationOptions) {
        this.specificationOptions = specificationOptions;
    }

    @Override
    public String toString() {
        return "Specification{" +
                "specification=" + specification +
                ", specificationOptions=" + specificationOptions +
                '}';
    }
}
