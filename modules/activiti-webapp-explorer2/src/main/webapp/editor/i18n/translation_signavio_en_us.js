ORYX.I18N.PropertyWindow.dateFormat = "d/m/y";

// ORYX.I18N.View.East = "EN* Attributes";
ORYX.I18N.View.East = "Atributai";
// ORYX.I18N.View.West = "EN* Modeling Elements";
ORYX.I18N.View.West = "Modeliavimo elementai";

ORYX.I18N.Oryx.title	= "EN* Signavio";
ORYX.I18N.Oryx.pleaseWait = "EN* Please wait while the Signavio Process Editor is loading...";
// ORYX.I18N.Edit.cutDesc = "EN* Cuts the selection into the clipboard";
ORYX.I18N.Edit.cutDesc = "Iškerpa pasirinkimą į iškarpinę";
// ORYX.I18N.Edit.copyDesc = "EN* Copies the selection into the clipboard";
ORYX.I18N.Edit.copyDesc = "Kopijuoja pasirinkimą į iškarpinę";
// ORYX.I18N.Edit.pasteDesc = "EN* Pastes the clipboard to the canvas";
ORYX.I18N.Edit.pasteDesc = "Įklijuoja iškarpinės turinį į regadavimo sritį";
ORYX.I18N.ERDFSupport.noCanvas = "EN* The xml document has no canvas node included!";
ORYX.I18N.ERDFSupport.noSS = "EN* The Signavio Process Editor canvas node has no stencil set definition included!";
ORYX.I18N.ERDFSupport.deprText = "EN* Exporting to eRDF is not recommended anymore because the support will be stopped in future versions of the Signavio Process Editor. If possible, export the model to JSON. Do you want to export anyway?";
ORYX.I18N.Save.pleaseWait = "EN* Please wait<br/>while saving...";

ORYX.I18N.Save.saveAs = "EN* Save a copy...";
ORYX.I18N.Save.saveAsDesc = "EN* Save a copy...";
ORYX.I18N.Save.saveAsTitle = "EN* Save a copy...";
ORYX.I18N.Save.savedAs = "EN* Copy saved";
ORYX.I18N.Save.savedDescription = "EN* The process diagram is stored under";
ORYX.I18N.Save.notAuthorized = "EN* You are currently not logged in. Please <a href='/p/login' target='_blank'>log in</a> in a new window so that you can save the current diagram."
ORYX.I18N.Save.transAborted = "EN* The saving request took too long. You may use a faster internet connection. If you use wireless LAN, please check the strength of your connection.";
ORYX.I18N.Save.noRights = "EN* You do not have the required rights to store that model. Please check in the <a href='/p/explorer' target='_blank'>Signavio Explorer</a>, if you still have the rights to write in the target directory.";
ORYX.I18N.Save.comFailed = "EN* The communication with the Signavio server failed. Please check your internet connection. If the problem resides, please contact the Signavio Support via the envelope symbol in the toolbar.";
ORYX.I18N.Save.failed = "EN* Something went wrong when trying to save your diagram. Please try again. If the problem resides, please contact the Signavio Support via the envelope symbol in the toolbar.";
ORYX.I18N.Save.exception = "EN* Some exceptions are raised while trying to save your diagram. Please try again. If the problem resides, please contact the Signavio Support via the envelope symbol in the toolbar.";
ORYX.I18N.Save.retrieveData = "EN* Please wait, data is retrieving.";

/** New Language Properties: 10.6.09*/
if(!ORYX.I18N.ShapeMenuPlugin) ORYX.I18N.ShapeMenuPlugin = {};
ORYX.I18N.ShapeMenuPlugin.morphMsg = "EN* Transform shape";
ORYX.I18N.ShapeMenuPlugin.morphWarningTitleMsg = "EN* Transform shape";
ORYX.I18N.ShapeMenuPlugin.morphWarningMsg = "EN* There are child shape which can not be contained in the transformed element.<br/>Do you want to transform anyway?";

if (!Signavio) { var Signavio = {}; }
if (!Signavio.I18N) { Signavio.I18N = {} }
if (!Signavio.I18N.Editor) { Signavio.I18N.Editor = {} }

if (!Signavio.I18N.Editor.Linking) { Signavio.I18N.Editor.Linking = {} }
Signavio.I18N.Editor.Linking.CreateDiagram = "EN* Create a new diagram";
Signavio.I18N.Editor.Linking.UseDiagram = "EN* Use existing diagram";
Signavio.I18N.Editor.Linking.UseLink = "EN* Use web link";
Signavio.I18N.Editor.Linking.Close = "EN* Close";
Signavio.I18N.Editor.Linking.Cancel = "EN* Cancel";
Signavio.I18N.Editor.Linking.UseName = "EN* Adopt diagram name";
Signavio.I18N.Editor.Linking.UseNameHint = "EN* Replaces the current name of the modeling element ({type}) with the name of the linked diagram.";
Signavio.I18N.Editor.Linking.CreateTitle = "EN* Establish link";
Signavio.I18N.Editor.Linking.AlertSelectModel = "EN* You have to select a model.";
Signavio.I18N.Editor.Linking.ButtonLink = "EN* Link diagram";
Signavio.I18N.Editor.Linking.LinkNoAccess = "EN* You have no access to this diagram.";
Signavio.I18N.Editor.Linking.LinkUnavailable = "EN* The diagram is unavailable.";
Signavio.I18N.Editor.Linking.RemoveLink = "EN* Remove link";
Signavio.I18N.Editor.Linking.EditLink = "EN* Edit Link";
Signavio.I18N.Editor.Linking.OpenLink = "EN* Open";
Signavio.I18N.Editor.Linking.BrokenLink = "EN* The link is broken!";
Signavio.I18N.Editor.Linking.PreviewTitle = "EN* Preview";

if(!Signavio.I18N.Glossary_Support) { Signavio.I18N.Glossary_Support = {}; }
Signavio.I18N.Glossary_Support.renameEmpty = "EN* No dictionary entry";
Signavio.I18N.Glossary_Support.renameLoading = "EN* Searching...";

/** New Language Properties: 08.09.2009*/
if(!ORYX.I18N.PropertyWindow) ORYX.I18N.PropertyWindow = {};
ORYX.I18N.PropertyWindow.oftenUsed = "EN* Main properties";
ORYX.I18N.PropertyWindow.moreProps = "EN* More properties";

ORYX.I18N.PropertyWindow.btnOpen = "EN* Open";
ORYX.I18N.PropertyWindow.btnRemove = "EN* Remove";
ORYX.I18N.PropertyWindow.btnEdit = "EN* Edit";
ORYX.I18N.PropertyWindow.btnUp = "EN* Move up";
ORYX.I18N.PropertyWindow.btnDown = "EN* Move down";
ORYX.I18N.PropertyWindow.createNew = "EN* Create new";

if(!ORYX.I18N.PropertyWindow) ORYX.I18N.PropertyWindow = {};
// ORYX.I18N.PropertyWindow.oftenUsed = "EN* Main attributes";
ORYX.I18N.PropertyWindow.oftenUsed = "Pagrindiniai atributai";
// ORYX.I18N.PropertyWindow.moreProps = "EN* More attributes";
ORYX.I18N.PropertyWindow.moreProps = "Daugiau atributų";
ORYX.I18N.PropertyWindow.characteristicNr = "EN* Cost &amp; Resource Analysis";
ORYX.I18N.PropertyWindow.meta = "EN* Custom attributes";

if(!ORYX.I18N.PropertyWindow.Category){ORYX.I18N.PropertyWindow.Category = {}}
ORYX.I18N.PropertyWindow.Category.popular = "EN* Main Attributes";
ORYX.I18N.PropertyWindow.Category.characteristicnr = "EN* Cost &amp; Resource Analysis";
ORYX.I18N.PropertyWindow.Category.others = "EN* More Attributes";
ORYX.I18N.PropertyWindow.Category.meta = "EN* Custom Attributes";

if(!ORYX.I18N.PropertyWindow.ListView) ORYX.I18N.PropertyWindow.ListView = {};
ORYX.I18N.PropertyWindow.ListView.title = "EN* Edit: ";
ORYX.I18N.PropertyWindow.ListView.dataViewLabel = "EN* Already existing entries.";
ORYX.I18N.PropertyWindow.ListView.dataViewEmptyText = "EN* No list entries.";
ORYX.I18N.PropertyWindow.ListView.addEntryLabel = "EN* Add a new entry";
ORYX.I18N.PropertyWindow.ListView.buttonAdd = "EN* Add";
ORYX.I18N.PropertyWindow.ListView.save = "EN* Save";
ORYX.I18N.PropertyWindow.ListView.cancel = "EN* Cancel";

if(!Signavio.I18N.Buttons) Signavio.I18N.Buttons = {};
Signavio.I18N.Buttons.save		= "EN* Save";
Signavio.I18N.Buttons.cancel 	= "EN* Cancel";
Signavio.I18N.Buttons.remove	= "EN* Remove";

if(!Signavio.I18N.btn) {Signavio.I18N.btn = {};}
Signavio.I18N.btn.btnEdit = "EN* Edit";
Signavio.I18N.btn.btnRemove = "EN* Remove";
Signavio.I18N.btn.moveUp = "EN* Move up";
Signavio.I18N.btn.moveDown = "EN* Move down";

if(!Signavio.I18N.field) {Signavio.I18N.field = {};}
Signavio.I18N.field.Url = "EN* URL";
Signavio.I18N.field.UrlLabel = "EN* Label";
