LAHWidgets
==========

Android library for general purpose widgets

 *  FileDialog: alert dialog to select a file
 
 *  MultipleSelectListActivity: extension of activity for user to select multiple items from a list; client should simply override save() method to handle the case when user selects the [Save] menu
 
 *  TextArea: reduced implementation of Android's TextView for efficiency and modularity
 
    The Android design for text widgets is a BAD design:
    
    (i)    It is not modular: the TextView is so overloaded with features (marquee, ellipsized, editable/non-editable, etc.) and EditText is a simple customization for the purpose of editing
     
    (ii)   It is inefficient: with all the extra checking, transformation, generation of new text, re-layouts, etc. It has very inefficient way of handling formatted text, disallowing for too long content with too many spans.
     
    (iii)  It is difficult to extend: with hidden API; requiring Java reflection to access. For example, it is difficult to introduce additional actions when a piece of text is selected or adding personalized dictionary.
    
    So I took the challenge of implementing a reduced version of TextView (by modifying its original source code). In particular, my TextArea is intended for one and only one purpose: presenting and allowing user to edit text in left-to-right language.
    
    -  No fanciful features from TextView such as marquee
    
    -  Text is editable by default and is left-to-right orientation
       
    -  A single input type (i.e. plain text; NO phone, email, number, password, whatsoever)
    
    -  No state-saving
    
    -  Content is supplied by and synchronized with generic data source, no extra wrapping when setText() is invoked
    
    -  Allow for a focusable range instead of the whole sequence
    
    -  Caching of TextPaint objects