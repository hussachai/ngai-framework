// This is the only public method.  Initialised like:
// jQuery (#tableid).tableFilter(options)
(function(jQuery) {
    jQuery.fn.tableFilter = function(_options) {

        var lastkeytime;
        var lastTimerID;
        var grid;
        var cancelQuickFind;
        var filterKey;
        var columnsToIgnore;

        // Cached controls
        var filters;
        var headers;
        var rows;

        var options = jQuery.extend(jQuery.fn.tableFilter.defaults, _options);

        this.each(function() {
            grid = jQuery(this);
            initialiseFilters();
        });

        function initialiseFilters() {
            filterKey = grid.attr('id') + '_filters';
            initialiseControlCaches();
            columnsToIgnore = headers.length - filters.length;
            registerListenersOnFilters();
            setFilterWidths();
            loadFiltersFromCookie();
        }

        function registerListenersOnFilters() {
            filters.filter('input').keyup(onTableFilterChanged);
            filters.filter('select').change(onTableFilterChanged);
            if (options.clearFiltersControls) {
                for (var i = 0; i < options.clearFiltersControls.length; i++) {
                    options.clearFiltersControls[i].click(function() {
                        clearAllFilters();
                        return false;
                    });
                }
            }
            if (!options.additionalFilterTriggers) return;
            for (var i = 0; i < options.additionalFilterTriggers.length; i++) {
                var f = options.additionalFilterTriggers[i];
                switch (f.attr('type')) {
                    case 'select-one':
                        f.change(onTableFilterChanged);
                        break;
                    case 'text':
                        f.attr('title', jQuery.fn.tableFilter.filterToolTipMessage);
                        f.keyup(onTableFilterChanged);
                        break;
                    case 'checkbox':
                        f.click(onTableFilterChanged);
                        break;
                    default:
                        throw 'Filter type ' + f.attr('type') + ' is not supported';
                }
            }
        }

        function clearAllFilters() {
            filters.val('');
            if (options.additionalFilterTriggers) {
                for (var i = 0; i < options.additionalFilterTriggers.length; i++) {
                    var f = options.additionalFilterTriggers[i];
                    switch (f.attr('type')) {
                        case 'text':
                            f.val('');
                            break;
                        case 'checkbox':
                            f.attr('checked', false);
                            break;
                        default:
                            throw 'Filter type ' + f.attr('type') + ' is not supported';
                    }
                }
            }
            quickFindImpl();
        }

        function initialiseControlCaches() {
            headers = grid.children("thead tr:first th");
            rows = jQuery('#' + grid.attr('id') + ' tbody tr');
            buildFiltersRow();
            filters = grid.find("thead tr:last").find('input,select');
        }

        function buildFiltersRow() {
            var tr = $("<tr class'filters'></tr>");
            for (var i = 0; i < headers.length; i++) {
                var header = $(headers[i]);
                var headerText = header.text();
                var td = $(headerText.length > 1
                    ? '<td>' + getFilterStr(i, header) + '</td>'
                    : '<td>&nbsp;</td>');
                tr.append(td);

            }
            grid.children("thead").append(tr);
        }

        function getFilterStr(colIdx, header) {
            var filterType = header.attr('filter-type');
            if (!filterType) filterType = 'text';
            switch (filterType) {
                case 'text': return "<input type='text' id='filter_" + colIdx + "' class='filter' title='" + jQuery.fn.tableFilter.filterToolTipMessage + "'/>";
                case 'ddl': return getSelectFilter(colIdx, header);
                default: throw 'filter-type: ' + filterType + ' is not supported';
            }
        }

        function getSelectFilter(colIdx, header) {
            var html = "<select id='filter_" + colIdx + "' class='filter'><option value=''>Select...</option>";
            var cells = rows.find('td:nth-child(' + (colIdx + 1) + ')');
            var values = [];
            jQuery.each(cells, function() {
                var txt = $(this).text();
                if (!txt || txt === '&nbsp;') { return; }
                for (var i = 0; i < values.length; i++) { if (values[i] === txt) { return; } } // Contains
                values.push(txt);
            });
            values.sort();
            jQuery.each(values, function() {
                html += '<option value="' + this + '">' + this + '</option>';
            });
            html += '</select>';
            return html;
        }

        function loadFiltersFromCookie() {
            if (!jQuery.cookie) { return; }

            var filterState = jQuery.cookie(filterKey);
            if (!filterState) { return; }
            filterState = filterState.split(';');
            for (var i = 0; i < filterState.length; i++) {
                var state = filterState[i].split(',');
                filterState[i] = { id: state[0], value: state[3], idx: state[1], type: state[2] };
            }
            applyFilterStates(filterState, true);
        }

        function setFilterWidths() {
            for (var i = 0; i < filters.length; i++) {
                var f = jQuery(filters[i]);
                var width = jQuery(headers[i + columnsToIgnore]).width();
                f.width(width);
            }
        }

        function onTableFilterChanged(e) {
            lastkeytime = new Date().getTime();
            quickFindTimer();
        }

        function quickFindTimer() {
            if (lastTimerID) { clearTimeout(lastTimerID); }
            cancelQuickFind = true;

            var curtime = new Date().getTime();
            if (curtime - lastkeytime >= options.filterDelay) {
                quickFindImpl();
            } else {
                lastTimerID = setTimeout(quickFindTimer, options.filterDelay / 3);
            }
        }

        // TODO: Can this be optimised??
        function quickFindImpl() {
            cancelQuickFind = false;
            clearTimeout(lastTimerID);
            var filterStates = getFilterStates();
            applyFilterStates(filterStates, false);
            saveFiltersToCookie(filterStates);
        }

        function getFilterStates() {
            var filterStates = new Array();

            for (var i = 0; i < filters.length; i++) {
                var state = getFilterStateForFilter(jQuery(filters[i]));
                if (state) { filterStates[filterStates.length] = state; }
            }

            if (!options.additionalFilterTriggers) return filterStates;

            for (var i = 0; i < options.additionalFilterTriggers.length; i++) {
                var state = getFilterStateForFilter(options.additionalFilterTriggers[i]);
                if (state) filterStates[filterStates.length] = state;
            }
            return filterStates;
        }

        function getFilterStateForFilter(filter) {
            var type = filter.attr('type');
            var value;
            switch (type) {
                case 'text':
                    value = filter.val() === null ? null : filter.val().toLowerCase();
                    break;
                case 'select-one':
                    value = filter.val() === null ? null : filter.val();
                    break;
                case 'checkbox':
                    value = filter.attr('checked');
                    break;
                default:
                    throw 'Filter type ' + type + ' is not supported';
            }
            if (value === null || value.length <= 0) { return null; }
            var idx = getColumnIndexOfCurrentFilter(filter);
            return {
                id: filter.attr('id'),
                value: value.toString(),
                idx: idx,
                type: filter.attr('type')
            };
        }

        function saveFiltersToCookie(filterState) {
            if (!jQuery.cookie) { return; }
            var val = [];
            for (var i = 0; i < filterState.length; i++) {
                if (val.length > 0) val.push(';');
                var state = filterState[i];
                val.push(state.id);
                val.push(',');
                val.push(state.idx);
                val.push(',');
                val.push(state.type);
                val.push(',');
                val.push(state.value);
            }
            val = val.join('');
            jQuery.cookie(filterKey, val);
        }

        function applyFilterStates(filterStates, setValueOnFilter) {
            clearRowFilteredStates();
            if ((!filterStates || filterStates.length) === 0 && (options.matchingRow === null || options.matchingCell)) {
                hideRowsThatDoNotMatchAnyFiltres();
                return;
            }
            if (filterStates === null || filterStates.length === 0) { applyStateToRows(null); }
            else {
                for (var i = 0; i < filterStates.length; i++) {
                    var state = filterStates[i];
                    if (setValueOnFilter && state.type && state.id) {
                        switch (state.type) {
                            case 'select-one':
                            case 'text':
                                jQuery('#' + state.id).val(state.value);
                                break;
                            case 'checkbox':
                                jQuery('#' + state.id).attr('checked', state.value === 'true');
                                break;
                            default:
                                throw 'Filter type ' + state.type + ' is not supported';
                        }
                    }
                    applyStateToRows(state);
                }
            }

            hideRowsThatDoNotMatchAnyFiltres();
        }

        function clearRowFilteredStates() {
            rows.removeAttr('filtermatch');
        }

        function applyStateToRows(filterState) {
            var normalisedTokens = getNormalisedSearchTokensForState(filterState);
            var colidx = filterState === null ? -1 : filterState.idx;
            for (var i = 0; i < rows.length; i++) {
                if (cancelQuickFind) return;
                var tr = jQuery(rows[i]);
                if (tr.attr('filtermatch')) { continue; }
                if (!doesRowContainText(filterState, tr, normalisedTokens, colidx)) { tr.attr('filtermatch', 'false'); }
            }
        }

        function getNormalisedSearchTokensForState(state) {
            if (state === null) { return null; }
            switch (state.type) {
                case 'select-one':
                    return [state.value];
                case 'text':
                    return parseSearchTokens(state.value);
                case 'checkbox':
                    return null;
                default:
                    throw 'Filter type ' + f.attr('type') + ' is not supported';
            }
        }

        function hideRowsThatDoNotMatchAnyFiltres() {
            for (var i = 0; i < rows.length; i++) {
                if (cancelQuickFind) return;
                var tr = jQuery(rows[i]);
                if (tr.attr('filtermatch') === 'false') {
                    tr.hide();
                }
                else {
                    tr.show();
                }
            }
        }

        function getColumnIndexOfCurrentFilter(filter) {
            var filterCell = filter.parent('td');
            if (!filterCell || filterCell.length <= 0) { return -1; }
            var filterRow = filterCell.parent();
            return filterRow.children('td').index(filterCell);
        }

        function doesRowContainText(state, tr, textTokens, columnIdx) {
            var cells = tr.children('td');
            if (columnIdx < 0) {
                for (var j = columnsToIgnore; j < cells.length; j++) {
                    var cell = jQuery(cells[j]);
                    if (doesCellContainText(state, cell, textTokens)) { return checkMatchingRowCallback(state, tr, textTokens); }
                }
                return false;
            } else { return doesCellContainText(state, jQuery(cells[columnIdx]), textTokens) && checkMatchingRowCallback(state, tr, textTokens); }
        }

        function checkMatchingRowCallback(state, tr, textTokens) {
            if (!options.matchingRow) return true;
            return options.matchingRow(state, tr, textTokens);
        }

        function doesCellContainText(state, td, textTokens) {
            var text = td.text();
            if (!doesTextMatchTokens(text, textTokens, state != null && state.type === 'select-one')) { return false; }
            return !options.matchingCell || options.matchingCell(state, td, textTokens);
        }

        /*************************************************
        SEARCH FUNCTIONS
        TODO: Support quotes (phrases) (")
        **************************************************/

        var precedences;

        function parseSearchTokens(text) {
            if (!text) { return null; }
            if (!precedences) {
                precedences = new Object();
                precedences.or = 1;
                precedences.and = 2;
                precedences.not = 3;
            }
            text = text.toLowerCase();
            var normalisedTokens = normaliseExpression(text);
            normalisedTokens = allowFriendlySearchTerms(normalisedTokens);
            var asPostFix = convertExpressionToPostFix(normalisedTokens);
            var postFixTokens = asPostFix.split('|');
            return postFixTokens;
        }

        function normaliseExpression(text) {
            var textTokens = getTokensFromExpression(text);
            var normalisedTokens = new Array();

            for (var i = 0; i < textTokens.length; i++) {
                var token = textTokens[i];
                var parenthesisIdx = token.indexOf('(');
                while (parenthesisIdx != -1) {
                    if (parenthesisIdx > 0) {
                        normalisedTokens[normalisedTokens.length] = token.substring(0, parenthesisIdx);
                    }
                    normalisedTokens[normalisedTokens.length] = '(';
                    token = token.substring(parenthesisIdx + 1);
                    parenthesisIdx = token.indexOf('(');
                }

                parenthesisIdx = token.indexOf(')');

                while (parenthesisIdx != -1) {
                    if (parenthesisIdx > 0) {
                        normalisedTokens[normalisedTokens.length] = token.substring(0, parenthesisIdx);
                    }

                    normalisedTokens[normalisedTokens.length] = ')';
                    token = token.substring(parenthesisIdx + 1);
                    parenthesisIdx = token.indexOf(')');
                }

                if (token.length > 0) { normalisedTokens[normalisedTokens.length] = token; }
            }
            return normalisedTokens;
        }

        function getTokensFromExpression(exp) {
            var regex = /([^"^\s]+)\s*|"([^"]+)"\s*/g;
            var matches = exp.match(regex);
            for (var i = 0; i < matches.length; i++) { matches[i] = trim(matches[i]).replace(/"/g, ''); } // Hack the above regex is returning matches with quotes and trailing spaces (even tho the groups look correct???)
            return matches;
        }

        function allowFriendlySearchTerms(tokens) {
            var newTokens = new Array();
            var lastToken;

            for (var i = 0; i < tokens.length; i++) {
                var token = tokens[i];
                if (!token || token.length === 0) { continue; }
                if (token.indexOf('-') === 0) {
                    token = 'not';
                    tokens[i] = tokens[i].substring(1);
                    i--;
                }
                if (!lastToken) {
                    newTokens[newTokens.length] = token;
                } else {
                    if (lastToken != '(' && lastToken != 'not' && lastToken != 'and' && lastToken != 'or' && token != 'and' && token != 'or' && token != ')') {
                        newTokens[newTokens.length] = 'and';
                    }
                    newTokens[newTokens.length] = token;
                }
                lastToken = token;
            }
            return newTokens;
        }

        function convertExpressionToPostFix(normalisedTokens) {
            var postFix = '';
            var stackOps = new Array();
            var stackOperator;
            for (var i = 0; i < normalisedTokens.length; i++) {
                var token = normalisedTokens[i];
                if (token.length === 0) continue;
                if (token != 'and' && token != 'or' && token != 'not' && token != '(' && token != ')') {
                    postFix = postFix + '|' + token;
                }
                else {
                    if (stackOps.length === 0 || token === '(') {
                        stackOps.push(token);
                    }
                    else {
                        if (token === ')') {
                            stackOperator = stackOps.pop();
                            while (stackOperator != '(') {
                                postFix = postFix + '|' + stackOperator;
                                stackOperator = stackOps.pop();
                            }
                        }
                        else if (stackOps[stackOps.length - 1] === '(') {
                            stackOps.push(token);
                        } else {
                            while (stackOps.length != 0) {
                                if (stackOps[stackOps.length - 1] === '(') { break; }
                                if (precedences[stackOps[stackOps.length - 1]] > precedences[token]) {
                                    stackOperator = stackOps.pop();
                                    postFix = postFix + '|' + stackOperator;
                                }
                                else { break; }
                            }
                            stackOps.push(token);
                        }
                    }
                }
            }
            while (stackOps.length > 0) { postFix = postFix + '|' + stackOps.pop(); }
            return postFix.substring(1);
        }

        function trim(str) { return str.replace(/^\s\s*/, '').replace(/\s\s*$/, ''); }

        function doesTextMatchTokens(textToMatch, postFixTokens, exactMatch) {
            if (!postFixTokens) return true;
            textToMatch = exactMatch ? textToMatch : textToMatch.toLowerCase();
            var stackResult = new Array();
            var stackResult1;
            var stackResult2;

            for (var i = 0; i < postFixTokens.length; i++) {
                token = postFixTokens[i];
                if (token != 'and' && token != 'or' && token != 'not') {
                    stackResult.push(exactMatch ? textToMatch === token : textToMatch.indexOf(token) >= 0);
                }
                else {

                    if (token === 'and') {
                        stackResult1 = stackResult.pop();
                        stackResult2 = stackResult.pop();
                        stackResult.push(stackResult1 && stackResult2);
                    }
                    else if (token === 'or') {
                        stackResult1 = stackResult.pop();
                        stackResult2 = stackResult.pop();

                        stackResult.push(stackResult1 || stackResult2);
                    }
                    else if (token === 'not') {
                        stackResult1 = stackResult.pop();
                        stackResult.push(!stackResult1);
                    }
                }
            }
            return stackResult.length === 1 && stackResult.pop();
        }

        /*************************************************
        SEARCH FUNCTIONS TESTS
        **************************************************/
        // runTests(); // UNCOMMENT FOR TESTS
        function runTests() {
            testArgumentParsing();
            testSimpleANDMatches();
            testSimpleORMatches();
            testSimpleNOTMatches();
            testSimpleGroupMatches();
            testSimpleQuoteMatches();
            // TODO: Add complex queries
            window.alert('All Tests Passed');
        }

        function testArgumentParsing() {
            var tokens1 = parseSearchTokens('text1 and text2');
            var tokens2 = parseSearchTokens('text1 text2');
            assertArraysAreSame(tokens1, tokens2);

            var tokens1 = parseSearchTokens('not text2');
            var tokens2 = parseSearchTokens('-text2');
            assertArraysAreSame(tokens1, tokens2);
        }

        function assertArraysAreSame(arr1, arr2) {
            if (arr1.length != arr2.length) throw new Error('ERROR: assertArraysAreSame:1');
            for (var i = 0; i < arr1.length; i++) {
                if (arr1[i] != arr2[i]) throw new Error('ERROR: assertArraysAreSame:2');
            }
        }

        function testSimpleANDMatches() {
            var tokens1 = parseSearchTokens('text1 and text2');

            if (doesTextMatchTokens("text1", tokens1)) throw new Error('ERROR: testAndMatches:1');
            if (doesTextMatchTokens("text1 text3", tokens1)) throw new Error('ERROR: testAndMatches:2');
            if (!doesTextMatchTokens("text1 text2", tokens1)) throw new Error('ERROR: testAndMatches:3');
            if (!doesTextMatchTokens("text2 text1", tokens1)) throw new Error('ERROR: testAndMatches:4');
            if (!doesTextMatchTokens("text2 text 3text1", tokens1)) throw new Error('ERROR: testAndMatches:5');
        }

        function testSimpleORMatches() {
            var tokens1 = parseSearchTokens('text1 or text2');

            if (!doesTextMatchTokens("text1", tokens1)) throw new Error('ERROR: testSimpleORMatches:1');
            if (!doesTextMatchTokens("text1 text3", tokens1)) throw new Error('ERROR: testSimpleORMatches:2');
            if (!doesTextMatchTokens("text1 text2", tokens1)) throw new Error('ERROR: testSimpleORMatches:3');
            if (!doesTextMatchTokens("text2 text1", tokens1)) throw new Error('ERROR: testSimpleORMatches:4');
            if (!doesTextMatchTokens("text2 text 3text1", tokens1)) throw new Error('ERROR: testSimpleORMatches:5');
            if (doesTextMatchTokens("text3 text4", tokens1)) throw new Error('ERROR: testSimpleORMatches:6');
        }

        function testSimpleNOTMatches() {
            var tokens1 = parseSearchTokens('not text2');
            if (!doesTextMatchTokens("text1", tokens1)) throw new Error('ERROR: testSimpleNOTMatches:1');
            if (doesTextMatchTokens("text1 text2", tokens1)) throw new Error('ERROR: testSimpleNOTMatches:2');
        }

        function testSimpleGroupMatches() {
            var tokens1 = parseSearchTokens('(text1 and text2) or text3');
            if (doesTextMatchTokens("text1", tokens1)) throw new Error('ERROR: testSimpleGroupMatches:1');
            if (!doesTextMatchTokens("text1 text2", tokens1)) throw new Error('ERROR: testSimpleGroupMatches:2');
            if (!doesTextMatchTokens("text3", tokens1)) throw new Error('ERROR: testSimpleGroupMatches:2');
            if (!doesTextMatchTokens("text33", tokens1)) throw new Error('ERROR: testSimpleGroupMatches:3');
        }

        function testSimpleQuoteMatches() {
            var tokens1 = parseSearchTokens('"text1 is not text2" t3e3x3t3');

            if (doesTextMatchTokens("text1 not is text2 t3e3x3t3", tokens1)) throw new Error('ERROR: testSimpleQuoteMatches:1');
            if (doesTextMatchTokens("text1 is not t3e3x3t3 text3", tokens1)) throw new Error('ERROR: testSimpleQuoteMatches:2');
            if (!doesTextMatchTokens("this will match text1 is not text2 yet3e3x3t3ssss ", tokens1)) throw new Error('ERROR: testSimpleQuoteMatches:3');
        }
    };

    jQuery.fn.tableFilter.filterToolTipMessage = "Quotes (\") match phrases. (not) excludes a match from the results. (or) can be used to do Or searches. I.e. [red or blue] will match either red or blue.";

    jQuery.fn.tableFilter.defaults = {
        additionalFilterTriggers: [],
        clearFiltersControls: [],
        matchingRow: null,
        matchingCell: null,
        filterDelay: 200
    };

})(jQuery);