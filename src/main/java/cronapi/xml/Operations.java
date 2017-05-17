package cronapi.xml;

import java.io.File;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import cronapi.CronapiMetaData;
import cronapi.Var;
import cronapi.CronapiMetaData.CategoryType;
import cronapi.CronapiMetaData.ObjectType;

/**
 * Classe que representa ...
 * 
 * @author Rodrigo Reis
 * @version 1.0
 * @since 2017-03-29
 *
 */
@CronapiMetaData(category = CategoryType.XML, categoryTags = { "XML" })
public class Operations {

	// 	Abrir XML de um arquivo
	@CronapiMetaData(type = "function", name = "{{XMLOpenFromFileName}}", nameTags = {
			"XMLOpenFromFile" }, description = "{{XMLOpenFromFileDescription}}", params = {
					"{{XMLOpenFromFileParam0}}" }, paramsType = { ObjectType.OBJECT }, returnType = ObjectType.OBJECT)
	public static final Var XMLOpenFromFile(Var absPath) throws Exception {
		File fileCasted = new File(absPath.getObjectAsString());
		SAXBuilder builder = new SAXBuilder();
		return new Var(builder.build(fileCasted));
	}

	@CronapiMetaData(type = "function", name = "{{XMLOpenName}}", nameTags = {
			"XMLOpen" }, description = "{{XMLOpenDescription}}", params = {
					"{{XMLOpenParam0}}" }, paramsType = { ObjectType.STRING }, returnType = ObjectType.OBJECT)
	public static final Var XMLOpen(Var name) throws Exception {
		if (name.getObjectAsString() != "" && !name.equals(Var.VAR_NULL)) {
			SAXBuilder builder = new SAXBuilder();
			return new Var(builder.build(name.getObjectAsString()));
		}
		return Var.VAR_NULL;
	}

	@CronapiMetaData(type = "function", name = "{{XMLHasRootElementName}}", nameTags = {
			"XMLHasRootElement" }, description = "{{XMLHasRootElementDescription}}", params = {
					"{{XMLHasRootElementParam0}}" }, paramsType = {
							ObjectType.OBJECT }, returnType = ObjectType.BOOLEAN)
	public final static Var hasRootElement(Var document) {

		if (document.getObject() instanceof Document) {
			Document documentCasted = (Document) document.getObject();
			if (documentCasted.hasRootElement())
				return new Var(true);
			else
				return new Var(false);
		} else if (document.getObject() instanceof Element) {
			Element elementCasted = (Element) document.getObject();
			if (elementCasted.getDocument().hasRootElement())
				return new Var(true);
			else
				return new Var(false);
		}
		return new Var(false);
	}

	@CronapiMetaData(type = "function", name = "{{XMLGetRootElementName}}", nameTags = {
			"XMLGetRootElement" }, description = "{{XMLGetRootElementDescription}}", params = {
					"{{XMLGetRootElementParam0}}" }, paramsType = {
							ObjectType.OBJECT }, returnType = ObjectType.BOOLEAN)
	public final static Var getRootElement(Var document) {
		Document documentCasted = null;

		if (document.getObject() instanceof Document) {
			if (hasRootElement(document).getObjectAsBoolean()) {
				documentCasted = (Document) document.getObject();
				return new Var(documentCasted.getRootElement());
			} else {
				return Var.VAR_NULL;
			}
		} else if (document.getObject() instanceof Element) {
			Element elementCasted = (Element) document.getObject();
			if (hasRootElement(new Var(elementCasted.getDocument())).getObjectAsBoolean()) {
				return new Var(elementCasted.getDocument().getRootElement());
			}
		}
		return Var.VAR_NULL;

	}

	@CronapiMetaData(type = "function", name = "{{XMLDocumentToStringName}}", nameTags = {
			"XMLDocumentToString" }, description = "{{XMLDocumentToStringDescription}}", params = {
					"{{XMLDocumentToStringParam0}}" }, paramsType = {
							ObjectType.OBJECT }, returnType = ObjectType.STRING)
	public final static Var XMLDocumentToString(Var document) {
		if (!document.equals(Var.VAR_NULL)) {
			if (document.getObject() instanceof Document) {
				Document documentCasted = (Document) document.getObject();
				XMLOutputter xmlOut = new XMLOutputter();
				return new Var(xmlOut.outputString(documentCasted));
			} else if (document.getObject() instanceof Element) {
				Element documentCasted = (Element) document.getObject();
				XMLOutputter xmlOut = new XMLOutputter();
				return new Var(xmlOut.outputString(documentCasted.getDocument()));
			}
		}
		return Var.VAR_NULL;
	}
	
		@CronapiMetaData(type = "function", name = "{{XMLDocumentToStringName}}", nameTags = {
			"XMLDocumentToString" }, description = "{{XMLDocumentToStringDescription}}", params = {
					"{{XMLDocumentToStringParam0}}" }, paramsType = {
							ObjectType.OBJECT }, returnType = ObjectType.STRING)
	public final static Var XMLElementToString(Var document) {
		if (!document.equals(Var.VAR_NULL)) {
			if (document.getObject() instanceof Element) {
				Element documentCasted = (Element) document.getObject();
				XMLOutputter xmlOut = new XMLOutputter();
				return new Var(xmlOut.outputString(documentCasted));
			}
		}
		return Var.VAR_NULL;
	}

	@CronapiMetaData(type = "function", name = "{{XMLcreateElementInsideName}}", nameTags = {
			"XMLcreateElementInside" }, description = "{{XMLcreateElementInsideDescription}}", params = {
					"{{XMLcreateElementInsideParam0}}", "{{XMLcreateElementInsideParam1}}",
					"{{XMLcreateElementInsideParam2}}" }, paramsType = { ObjectType.OBJECT, ObjectType.STRING,
							ObjectType.STRING }, returnType = ObjectType.BOOLEAN)
	public final static Var XMLcreateElementInside(Var parent, Var name, Var value) {
		if (!parent.equals(Var.VAR_NULL) && !name.equals(Var.VAR_NULL)) {
			if (parent.getObject() instanceof Element) {
				Element parentCasted = (Element) parent.getObject();
				Element newElement = new Element(name.getObjectAsString());
				newElement.setText(value.getObjectAsString());
				parentCasted.getChildren().add(newElement);
				return new Var(true);
			} else
				return new Var(false);
		}
		return new Var(false);
	}

	@CronapiMetaData(type = "function", name = "{{XMLGetElementValueName}}", nameTags = {
			"XMLGetElementValue" }, description = "{{XMLGetElementValueDescription}}", params = {
					"{{XMLGetElementValueParam0}}" }, paramsType = {
							ObjectType.OBJECT }, returnType = ObjectType.STRING)
	public static final Var XMLGetElementValue(Var element) throws Exception {

		if (element.getObject() instanceof Element) {
			Element elementCasted = (Element) element.getObject();
			return new Var(elementCasted.getText());
		} else if (element.getType() == Var.Type.LIST) {
			String result = "";
			for (Var v : element.getObjectAsList()) {
				if (v.getObject() instanceof Element)
					result = result + (((Element) v.getObject()).getText());
				else {
					result = result + v.getObjectAsString();
				}
			}
			return new Var(result);
		}
		return new Var("");
	}

	@CronapiMetaData(type = "function", name = "{{XMLGetChildElementName}}", nameTags = {
			"XMLGetChildElement" }, description = "{{XMLGetChildElementDescription}}", params = {
					"{{XMLGetChildElementParam0}}", "{{XMLGetChildElementParam1}}" }, paramsType = { ObjectType.OBJECT,
							ObjectType.OBJECT }, returnType = ObjectType.LIST)
	public static final Var XMLGetChildElement(Var element, Var child) throws Exception {
		if (!element.equals(Var.VAR_NULL) && !child.equals(Var.VAR_NULL)) {
			if (element.getObject() instanceof Element) {
				Element elementCasted = (Element) element.getObject();
				if (child.getObject() instanceof Element) {
					Element childCasted = (Element) child.getObject();
					return new Var(elementCasted.getChildren(childCasted.getName()));
				} else if (child.getObject() instanceof String) {
					return new Var(elementCasted.getChildren(child.getObjectAsString()));
				}
			} else if (element.getObject() instanceof Document) {
				if (child.getObject() instanceof Element) {

				} else if (child.getObject() instanceof String) {
					Document documentCasted = (Document) element.getObject();
					if (documentCasted.getRootElement().getName() == child.getObjectAsString()) {
						return new Var(documentCasted.getRootElement());
					} else {
						return new Var(documentCasted.getRootElement().getChildren(child.getObjectAsString()));
					}
				}
			}
			return Var.VAR_NULL;
		}
		return Var.VAR_NULL;
	}

	@CronapiMetaData(type = "function", name = "{{XMLGetAttributeName}}", nameTags = {
			"XMLGetAttribute" }, description = "{{XMLGetAttributeDescription}}", params = { "{{XMLGetAttributeParam0}}",
					"{{XMLGetAttributeParam1}}" }, paramsType = { ObjectType.OBJECT,
							ObjectType.OBJECT }, returnType = ObjectType.STRING)
	public static final Var XMLGetAttributeValue(Var element, Var attribute) throws Exception {
		if (!element.equals(Var.VAR_NULL) && (element.getObject() instanceof Element) && !attribute.equals(Var.VAR_NULL)
				&& attribute.getObjectAsString() != "") {
			Element elementCasted = (Element) element.getObject();
			return new Var(elementCasted.getAttributeValue(attribute.getObjectAsString()));
		}
		return Var.VAR_NULL;
	}

	// Alterar o valor de um Atributo XML
	@CronapiMetaData(type = "function", name = "{{XMLSetElementAttributeValueName}}", nameTags = {
			"XMLSetElementAttributeValue" }, description = "{{XMLSetElementValueDescription}}", params = {
					"{{XMLSetElementAttributeValueParam0}}", "{{XMLSetElementAttributeValueParam1}}",
					"{{XMLSetElementAttributeValueParam2}}"

	}, paramsType = { ObjectType.OBJECT, ObjectType.STRING, ObjectType.STRING })
	public static final void XMLSetElementAttributeValue(Var element, Var attributeName, Var value) throws Exception {
		if (!element.equals(Var.VAR_NULL) && !attributeName.equals(Var.VAR_NULL) && !value.equals(Var.VAR_NULL)) {
			if (element.getObject() instanceof Element) {
				Element elementCasted = (Element) element.getObject();
				elementCasted.setAttribute(attributeName.getObjectAsString(), value.getObjectAsString());
			} else
				throw new Exception();
		} else
			throw new Exception();
	}

	@CronapiMetaData(type = "function", name = "{{XMLGetParentElementName}}", nameTags = {
			"XMLGetParentElement" }, description = "{{XMLGetParentElementDescription}}", params = {
					"{{XMLGetParentElementParam0}}" }, paramsType = {
							ObjectType.OBJECT }, returnType = ObjectType.OBJECT)
	public static final Var XMLGetParentElement(Var element) throws Exception {
		if (!element.equals(Var.VAR_NULL)) {
			if (element.getObject() instanceof Element) {
				Element elementCasted = (Element) element.getObject();
				return new Var(elementCasted.getParentElement());
			} else if (element.getType() == Var.Type.LIST) {
				if (element.getObjectAsList().getFirst().getObject() instanceof Element) {
					return new Var(((Element) element.getObjectAsList().getFirst().getObject()).getParent());
				}
			}
		}
		return Var.VAR_NULL;
	}

	@CronapiMetaData(type = "function", name = "{{XMLGetElementTagNameName}}", nameTags = {
			"XMLGetElementTagName" }, description = "{{XMLGetElementTagNameDescription}}", params = {
					"{{XMLGetElementTagNameParam0}}" }, paramsType = {
							ObjectType.OBJECT }, returnType = ObjectType.STRING)
	public static final Var XMLGetElementTagName(Var element) throws Exception {
		if (!element.equals(Var.VAR_NULL)) {
			if (element.getObject() instanceof Element) {
				Element elementCasted = (Element) element.getObject();
				return new Var(elementCasted.getName());
			}
		}
		return Var.VAR_NULL;

	}

	//PAREI AQUI
	//Alterar o valor de um Elemento XML
	@CronapiMetaData(type = "function", name = "{{XMLSetElementValueName}}", nameTags = {
			"XMLSetElementValue" }, description = "{{XMLSetElementValueDescription}}", params = {
					"{{XMLSetElementValueParam0}}",
					"{{XMLSetElementValueParam1}}" }, paramsType = { ObjectType.OBJECT, ObjectType.STRING })
	public static final void XMLSetElementValue(Var element, Var value) throws Exception {
		if (!element.equals(Var.VAR_NULL) && !value.equals(Var.VAR_NULL)) {
			if (element.getObject() instanceof Element) {
				Element elementCasted = (Element) element.getObject();
				if (value.getObject() instanceof Element) {
					Element valueCasted = (Element) value.getObject();
					elementCasted.setText(valueCasted.getText());
				} else if (value.getType() == Var.Type.STRING) {
					elementCasted.setText(value.getObjectAsString());
				}
			} else
				throw new Exception();
		}
	}
}
