
@CronapiMetaData(category = CategoryType.LOGIC, categoryTags = {"Lógica", "Logic"})
public class Operations {

  @CronapiMetaData(type = "function", name = "{{isNullName}}", nameTags = {
      "isNullFunction"}, description = "{{isNullDescription}}", displayInline = true, returnType = ObjectType.BOOLEAN)
  public static final Var isNull(
      @ParamMetaData(type = ObjectType.OBJECT, description = "{{parameter}}") Var var) {
    return Var.valueOf(var.isNull());
  }

  @CronapiMetaData(type = "function", name = "{{isNullOrEmptyName}}", nameTags = {
      "isNullOrEmptyFunction"}, description = "{{isNullOrEmptyDescription}}", displayInline = true, returnType = ObjectType.BOOLEAN)
  public static final Var isNullOrEmpty(
      @ParamMetaData(type = ObjectType.OBJECT, description = "{{parameter}}") Var var) {

    return Var.valueOf(var.isEmptyOrNull());
  }

  @CronapiMetaData(type = "function", name = "{{isEmptyName}}", nameTags = {
      "isEmptyFunction"}, description = "{{isEmptyDescription}}", displayInline = true, returnType = ObjectType.BOOLEAN)
  public static final Var isEmpty(
      @ParamMetaData(type = ObjectType.OBJECT, description = "{{parameter}}") Var var) {
    return var.valueOf(var.isEmpty()) ;
  }
}